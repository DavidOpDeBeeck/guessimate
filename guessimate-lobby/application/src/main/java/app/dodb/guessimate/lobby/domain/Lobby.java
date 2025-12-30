package app.dodb.guessimate.lobby.domain;


import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.Emoji;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.KeepAliveEvent;
import app.dodb.guessimate.lobby.api.event.LobbyCreatedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyEvent;
import app.dodb.guessimate.lobby.api.event.ReactionClearedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionSetEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsDisabledEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.TimerDuration;
import app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRole;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.lobby.api.event.UsernameSetEvent;
import app.dodb.guessimate.lobby.port.UserConnectivityChecker;
import app.dodb.guessimate.session.api.command.AddEstimationCommand;
import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.smd.api.command.CommandGateway;
import app.dodb.smd.api.event.Event;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATION_COMPLETED;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.lobby.api.event.UserRole.OBSERVER;
import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;

public class Lobby {

    private final LobbyState state;
    private final List<Event> events = new ArrayList<>();

    public Lobby(LobbyState state) {
        this.state = requireNonNull(state);
    }

    public Lobby(SessionId sessionId, DeckTO deck) {
        this(new LobbyState(
            sessionId,
            deck,
            false,
            null,
            TimerDuration.DISABLED,
            false,
            null,
            null,
            ESTIMATING,
            new ArrayList<>()
        ));
        record(new LobbyCreatedEvent(sessionId.value()));
    }

    public void connect(UserId userId, String username) {
        record(new UserConnectedEvent(sessionId(), userId.value(), username));
        state.getAutoJoinRole().ifPresent(userRole -> setUserRole(userId, userRole));
    }

    public void disconnect(UserId userId) {
        var user = state.findUserBy(userId);
        record(new UserDisconnectedEvent(sessionId(), user.userId().value()));
    }

    public void setUserRole(UserId userId, UserRole role) {
        var user = state.findUserBy(userId);
        switch (role) {
            case ESTIMATOR -> {
                if (user.isEstimator()) {
                    return;
                }
                record(new UserRoleSetEvent(sessionId(), userId.value(), ESTIMATOR));
            }
            case OBSERVER -> {
                if (user.isObserver()) {
                    return;
                }
                clearEstimate(userId);
                record(new UserRoleSetEvent(sessionId(), user.userId().value(), OBSERVER));
            }
        }
    }

    public void setUserName(UserId userId, String username) {
        var user = state.findUserBy(userId);
        if (user.hasUsername(username)) {
            return;
        }
        record(new UsernameSetEvent(sessionId(), userId.value(), username));
    }

    public void setEstimate(UserId userId, String estimate) {
        if (state.getStatus() != ESTIMATING) {
            return;
        }
        if (!state.getDeck().contains(estimate)) {
            return;
        }

        var user = state.findUserBy(userId);
        if (user.hasEstimate(estimate)) {
            return;
        }
        record(new EstimateSetEvent(sessionId(), userId.value(), estimate));
    }

    public void clearAllEstimates() {
        state.getUserIds().forEach(this::clearEstimate);
    }

    public void clearEstimate(UserId userId) {
        var user = state.findUserBy(userId);
        if (user.estimate().isEmpty()) {
            return;
        }
        record(new EstimateClearedEvent(sessionId(), user.userId().value()));
    }

    public void startEstimation(Instant now) {
        clearAllEstimates();
        clearAllReactions();
        record(new EstimationStartedEvent(sessionId(), state.getTimerDuration().calculateExpiryTime(now).orElse(null)));
    }

    public void tryAutoCompleteEstimation(CommandGateway commandGateway) {
        if (state.getStatus() != ESTIMATING) {
            return;
        }
        if (state.isAutoReveal() && state.allEstimatorsHaveVoted()) {
            completeEstimation(commandGateway);
        }
        if (state.isTimerExpired()) {
            completeEstimation(commandGateway);
        }
    }

    public void completeEstimation(CommandGateway commandGateway) {
        List<String> estimates = state.getUsers().stream()
            .filter(User::isEstimator)
            .map(User::estimate)
            .flatMap(Optional::stream)
            .toList();

        if (estimates.isEmpty()) {
            return;
        }

        var estimationId = commandGateway.send(new AddEstimationCommand(state.getSessionId().value(), state.getDeck(), estimates));
        record(new EstimationCompletedEvent(sessionId(), estimationId, estimates));
    }

    public void setDeck(DeckTO deck) {
        if (state.getDeck().equals(deck)) {
            return;
        }
        clearAllEstimates();
        record(new DeckSetEvent(sessionId(), deck));
    }

    public void setAutoJoin(UserRole role) {
        if (state.hasAutoJoinRole(role)) {
            return;
        }
        record(new AutoJoinUpdatedEvent(sessionId(), role));
    }

    public void setAutoReveal(boolean enabled) {
        if (state.isAutoReveal() == enabled) {
            return;
        }
        record(enabled
            ? new AutoRevealEnabledEvent(sessionId())
            : new AutoRevealDisabledEvent(sessionId()));
    }

    public void setTimerDuration(TimerDuration duration, Instant now) {
        if (state.getTimerDuration() == duration) {
            return;
        }
        record(new TimerDurationSetEvent(sessionId(), duration));
        if (state.getStatus() == ESTIMATING) {
            record(new EstimationStartedEvent(sessionId(), state.getTimerDuration().calculateExpiryTime(now).orElse(null)));
        }
    }

    public void setReactionsEnabled(boolean enabled) {
        if (state.isReactionsEnabled() == enabled) {
            return;
        }
        record(enabled
            ? new ReactionsEnabledEvent(sessionId())
            : new ReactionsDisabledEvent(sessionId()));
    }

    public void clearAllReactions() {
        state.getUserIds().forEach(this::clearReaction);
    }

    public void clearReaction(UserId userId) {
        var user = state.findUserBy(userId);
        if (user.reaction().isEmpty()) {
            return;
        }
        record(new ReactionClearedEvent(sessionId(), user.userId().value()));
    }

    public void setReaction(UserId userId, Emoji reaction) {
        if (!state.isReactionsEnabled() || state.getStatus() != ESTIMATION_COMPLETED) {
            return;
        }

        var user = state.findUserBy(userId);
        if (user.role().isEmpty()) {
            return;
        }

        if (user.hasReaction(reaction)) {
            record(new ReactionClearedEvent(sessionId(), userId.value()));
        } else {
            record(new ReactionSetEvent(sessionId(), userId.value(), reaction));
        }
    }

    public void checkActivity(UserConnectivityChecker connectivityPort) {
        if (state.getUserIds().isEmpty()) {
            return;
        }

        state.getUserIds().stream()
            .filter(not(connectivityPort::isConnected))
            .forEach(this::disconnect);

        record(new KeepAliveEvent(sessionId()));
    }

    public List<Event> consumeEvents() {
        var copy = new ArrayList<>(events);
        events.clear();
        return copy;
    }

    private void record(LobbyEvent event) {
        state.apply(event);
        events.add(event);
    }

    private String sessionId() {
        return state.getSessionId().value();
    }
}
