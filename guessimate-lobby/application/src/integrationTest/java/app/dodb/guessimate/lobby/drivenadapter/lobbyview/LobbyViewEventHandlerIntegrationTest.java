package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.IntegrationTest;
import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionClearedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionSetEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsDisabledEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.lobby.api.event.UsernameSetEvent;
import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.smd.api.event.bus.EventBus;
import app.dodb.smd.api.query.QueryHandler;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.ANOTHER_USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.Emoji.THUMBS_UP;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATION_COMPLETED;
import static app.dodb.guessimate.lobby.api.event.TimerDuration.DISABLED;
import static app.dodb.guessimate.lobby.api.event.TimerDuration.THIRTY_SECONDS;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.lobby.api.event.UserRole.OBSERVER;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import(LobbyViewEventHandlerIntegrationTest.TestConfig.class)
class LobbyViewEventHandlerIntegrationTest {

    @TestConfiguration
    public static class TestConfig {
        @Component
        public static class StubDeckQueryHandler {
            @QueryHandler
            public DeckTO handle(FindDefaultDeckQuery query) {
                return aDeckTO();
            }
        }
    }

    @Inject
    EventBus eventBus;
    @Inject
    LobbyViewSpringRepository repository;

    @Test
    void handleSessionCreatedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.isAutoReveal()).isFalse();
        assertThat(actual.getAutoJoinRole()).isEmpty();
        assertThat(actual.getTimerDuration()).isEqualTo(DISABLED);
        assertThat(actual.isReactionsEnabled()).isFalse();
        assertThat(actual.getStatus()).isEqualTo(ESTIMATING);
    }

    @Test
    void handleDeckSetEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var newDeck = new DeckTO("new-deck", List.of("S", "M", "L"));
        eventBus.publish(new DeckSetEvent(SESSION_ID_VALUE, newDeck));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getDeck()).isEqualTo(newDeck);
    }

    @Test
    void handleAutoRevealEnabledEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new AutoRevealEnabledEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.isAutoReveal()).isTrue();
    }

    @Test
    void handleAutoRevealDisabledEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new AutoRevealEnabledEvent(SESSION_ID_VALUE));
        eventBus.publish(new AutoRevealDisabledEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.isAutoReveal()).isFalse();
    }

    @Test
    void handleAutoJoinUpdatedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new AutoJoinUpdatedEvent(SESSION_ID_VALUE, OBSERVER));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getAutoJoinRole()).contains(OBSERVER);
    }

    @Test
    void handleTimerDurationSetEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new TimerDurationSetEvent(SESSION_ID_VALUE, THIRTY_SECONDS));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getTimerDuration()).isEqualTo(THIRTY_SECONDS);
    }

    @Test
    void handleReactionsEnabledEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new ReactionsEnabledEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.isReactionsEnabled()).isTrue();
    }

    @Test
    void handleReactionsDisabledEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new ReactionsEnabledEvent(SESSION_ID_VALUE));
        eventBus.publish(new ReactionsDisabledEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.isReactionsEnabled()).isFalse();
    }

    @Test
    void handleEstimationStartedEvent() {
        var timerExpiresAt = Instant.now().plusSeconds(30);
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new EstimationStartedEvent(SESSION_ID_VALUE, timerExpiresAt));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(ESTIMATING);
        assertThat(actual.getTimerExpiresAt()).contains(timerExpiresAt);
    }

    @Test
    void handleEstimationCompletedEvent() {
        var estimationId = "estimation-123";
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new EstimationCompletedEvent(SESSION_ID_VALUE, estimationId, List.of("1", "2", "3")));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(ESTIMATION_COMPLETED);
        assertThat(actual.getPreviousEstimationId()).contains(estimationId);
    }

    @Test
    void handleUserConnectedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers()).hasSize(1);
        assertThat(actual.getUsers().getFirst().getUserId()).isEqualTo(USER_ID_VALUE);
        assertThat(actual.getUsers().getFirst().getUsername()).isEqualTo(USERNAME_VALUE);
    }

    @Test
    void handleUserDisconnectedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new UserDisconnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers()).isEmpty();
    }

    @Test
    void handleUserRoleSetEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers().getFirst().getRole()).contains(ESTIMATOR);
    }

    @Test
    void handleUsernameSetEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new UsernameSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ANOTHER_USERNAME_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers().getFirst().getUsername()).isEqualTo(ANOTHER_USERNAME_VALUE);
    }

    @Test
    void handleEstimateSetEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers().getFirst().getEstimate()).contains(ESTIMATE_VALUE);
    }

    @Test
    void handleEstimateClearedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE));
        eventBus.publish(new EstimateClearedEvent(SESSION_ID_VALUE, USER_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers().getFirst().getEstimate()).isEmpty();
    }

    @Test
    void handleReactionSetEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new ReactionSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, THUMBS_UP));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers().getFirst().getReaction()).contains(THUMBS_UP);
    }

    @Test
    void handleReactionClearedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new ReactionSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, THUMBS_UP));
        eventBus.publish(new ReactionClearedEvent(SESSION_ID_VALUE, USER_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsers().getFirst().getReaction()).isEmpty();
    }
}
