package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
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
import app.dodb.guessimate.session.api.deck.DeckTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATION_COMPLETED;

public record LobbyData(
    DeckTO deck,
    boolean autoReveal,
    UserRole autoJoinRole,
    TimerDuration timerDuration,
    boolean reactionsEnabled,
    String previousEstimationId,
    Instant timerExpiresAt,
    LobbyStatus status,
    List<UserData> users
) {

    LobbyData apply(DeckSetEvent event) {
        return new LobbyData(event.deck(), autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, users);
    }

    LobbyData apply(AutoRevealEnabledEvent event) {
        return new LobbyData(deck, true, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, users);
    }

    LobbyData apply(AutoRevealDisabledEvent event) {
        return new LobbyData(deck, false, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, users);
    }

    LobbyData apply(AutoJoinUpdatedEvent event) {
        return new LobbyData(deck, autoReveal, event.role(), timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, users);
    }

    LobbyData apply(TimerDurationSetEvent event) {
        return new LobbyData(deck, autoReveal, autoJoinRole, event.timerDuration(),
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, users);
    }

    LobbyData apply(ReactionsEnabledEvent event) {
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            true, previousEstimationId, timerExpiresAt, status, users);
    }

    LobbyData apply(ReactionsDisabledEvent event) {
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            false, previousEstimationId, timerExpiresAt, status, users);
    }

    LobbyData apply(EstimationStartedEvent event) {
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, event.timerExpiresAt(), ESTIMATING, users);
    }

    LobbyData apply(EstimationCompletedEvent event) {
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, event.estimationId(), timerExpiresAt, ESTIMATION_COMPLETED, users);
    }

    LobbyData apply(UserConnectedEvent event) {
        var updated = new ArrayList<>(users);
        updated.add(new UserData(event.userId(), event.username()));
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    LobbyData apply(UserDisconnectedEvent event) {
        var updated = new ArrayList<>(users);
        updated.removeIf(u -> u.userId().equals(event.userId()));
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    LobbyData apply(UserRoleSetEvent event) {
        var updated = users.stream()
            .map(u -> u.userId().equals(event.userId()) ? u.withRole(event.role()) : u)
            .toList();
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    LobbyData apply(UsernameSetEvent event) {
        var updated = users.stream()
            .map(u -> u.userId().equals(event.userId()) ? u.withUsername(event.username()) : u)
            .toList();
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    LobbyData apply(EstimateSetEvent event) {
        var updated = users.stream()
            .map(u -> u.userId().equals(event.userId()) ? u.withEstimate(event.estimate()) : u)
            .toList();
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    LobbyData apply(EstimateClearedEvent event) {
        var updated = users.stream()
            .map(u -> u.userId().equals(event.userId()) ? u.withEstimate(null) : u)
            .toList();
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    LobbyData apply(ReactionSetEvent event) {
        var updated = users.stream()
            .map(u -> u.userId().equals(event.userId()) ? u.withReaction(event.emoji()) : u)
            .toList();
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    LobbyData apply(ReactionClearedEvent event) {
        var updated = users.stream()
            .map(u -> u.userId().equals(event.userId()) ? u.withReaction(null) : u)
            .toList();
        return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
            reactionsEnabled, previousEstimationId, timerExpiresAt, status, updated);
    }

    public Optional<UserRole> getAutoJoinRole() {
        return Optional.ofNullable(autoJoinRole);
    }

    public Optional<String> getPreviousEstimationId() {
        return Optional.ofNullable(previousEstimationId);
    }

    public Optional<Instant> getTimerExpiresAt() {
        return Optional.ofNullable(timerExpiresAt);
    }

    // --- Builder for construction ---

    public static final class Builder {

        private DeckTO deck;
        private Boolean autoReveal;
        private UserRole autoJoinRole;
        private TimerDuration timerDuration;
        private Boolean reactionsEnabled;
        private String previousEstimationId;
        private Instant timerExpiresAt;
        private LobbyStatus status;
        private List<UserData> users;

        public Builder deck(DeckTO deck) {
            this.deck = deck;
            return this;
        }

        public Builder autoReveal(Boolean autoReveal) {
            this.autoReveal = autoReveal;
            return this;
        }

        public Builder autoJoinRole(UserRole autoJoinRole) {
            this.autoJoinRole = autoJoinRole;
            return this;
        }

        public Builder timerDuration(TimerDuration timerDuration) {
            this.timerDuration = timerDuration;
            return this;
        }

        public Builder reactionsEnabled(Boolean reactionsEnabled) {
            this.reactionsEnabled = reactionsEnabled;
            return this;
        }

        public Builder previousEstimationId(String previousEstimationId) {
            this.previousEstimationId = previousEstimationId;
            return this;
        }

        public Builder timerExpiresAt(Instant timerExpiresAt) {
            this.timerExpiresAt = timerExpiresAt;
            return this;
        }

        public Builder status(LobbyStatus status) {
            this.status = status;
            return this;
        }

        public Builder users(List<UserData> users) {
            this.users = users;
            return this;
        }

        public LobbyData build() {
            return new LobbyData(deck, autoReveal, autoJoinRole, timerDuration,
                reactionsEnabled, previousEstimationId, timerExpiresAt, status, users);
        }
    }
}
