package app.dodb.guessimate.lobby.drivenadapter.lobbymetricsview;

import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyCreatedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsDisabledEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.smd.api.event.EventHandler;
import app.dodb.smd.api.event.ProcessingGroup;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@ProcessingGroup("lobby_metrics_view")
public class LobbyMetricsViewEventHandler {

    private final LobbyMetricsViewSpringRepository repository;

    public LobbyMetricsViewEventHandler(LobbyMetricsViewSpringRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(LobbyCreatedEvent event) {
        repository.save(new LobbyMetricsView(event.sessionId(), Instant.now()));
    }

    @EventHandler
    public void on(UserConnectedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event, Instant.now()));
    }

    @EventHandler
    public void on(UserDisconnectedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event, Instant.now()));
    }

    @EventHandler
    public void on(EstimationStartedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event, Instant.now()));
    }

    @EventHandler
    public void on(EstimationCompletedEvent event) {
        updateView(event.sessionId(), view -> view.apply(event, Instant.now()));
    }

    @EventHandler
    public void on(EstimateSetEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(EstimateClearedEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(DeckSetEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(AutoRevealEnabledEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(AutoRevealDisabledEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(AutoJoinUpdatedEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(TimerDurationSetEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(ReactionsEnabledEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(ReactionsDisabledEvent event) {
        updateLastActivity(event.sessionId());
    }

    @EventHandler
    public void on(UserRoleSetEvent event) {
        updateLastActivity(event.sessionId());
    }

    private void updateLastActivity(String sessionId) {
        updateView(sessionId, view -> view.updateLastActivity(Instant.now()));
    }

    private void updateView(String sessionId, java.util.function.Consumer<LobbyMetricsView> updater) {
        repository.findById(sessionId)
            .ifPresent(view -> {
                updater.accept(view);
                repository.save(view);
            });
    }
}
