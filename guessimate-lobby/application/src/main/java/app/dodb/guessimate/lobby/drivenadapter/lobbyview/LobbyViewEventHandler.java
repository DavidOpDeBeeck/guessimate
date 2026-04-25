package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyCreatedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
import app.dodb.guessimate.lobby.api.event.ReactionClearedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionSetEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsDisabledEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.TimerDuration;
import app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.lobby.api.event.UsernameSetEvent;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.smd.api.event.EventHandler;
import app.dodb.smd.api.event.ProcessingGroup;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@ProcessingGroup("lobby_view")
public class LobbyViewEventHandler {

    private final QueryGateway queryGateway;
    private final LobbyViewSpringRepository repository;

    public LobbyViewEventHandler(QueryGateway queryGateway,
                                 LobbyViewSpringRepository repository) {
        this.queryGateway = queryGateway;
        this.repository = repository;
    }

    @EventHandler
    public void on(LobbyCreatedEvent event) {
        repository.save(new LobbyView(
            event.sessionId(),
            new LobbyData.Builder()
                .deck(queryGateway.send(new FindDefaultDeckQuery()))
                .autoReveal(false)
                .autoJoinRole(null)
                .timerDuration(TimerDuration.DISABLED)
                .reactionsEnabled(false)
                .previousEstimationId(null)
                .timerExpiresAt(null)
                .status(LobbyStatus.ESTIMATING)
                .users(new ArrayList<>())
                .build()
        ));
    }

    @EventHandler
    public void on(DeckSetEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(AutoRevealEnabledEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(AutoRevealDisabledEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(AutoJoinUpdatedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(TimerDurationSetEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(ReactionsEnabledEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(ReactionsDisabledEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(EstimationStartedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(EstimationCompletedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(UserConnectedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(UserDisconnectedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(UserRoleSetEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(UsernameSetEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(EstimateSetEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(EstimateClearedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(ReactionSetEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    @EventHandler
    public void on(ReactionClearedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event));
    }

    private void updateView(String sessionId, java.util.function.Consumer<LobbyView> updater) {
        repository.findById(sessionId)
            .ifPresent(view -> {
                updater.accept(view);
                repository.save(view);
            });
    }
}
