package app.dodb.guessimate.lobby.drivenadapter.lobbymetricsview;

import app.dodb.guessimate.lobby.IntegrationTest;
import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.smd.api.event.bus.EventBus;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.ANOTHER_USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.ANOTHER_USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATION_COMPLETED;
import static app.dodb.guessimate.lobby.api.event.TimerDuration.THIRTY_SECONDS;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class LobbyMetricsViewEventHandlerIntegrationTest {

    @Inject
    EventBus eventBus;
    @Inject
    LobbyMetricsViewSpringRepository repository;

    @Test
    void onSessionCreatedEvent_createsMetricsView() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getLastActivity()).isNotNull();
        assertThat(actual.getLastActivity()).isEqualTo(actual.getCreatedAt());
        assertThat(actual.getConnectedUserCount()).isZero();
        assertThat(actual.getEstimationsCompleted()).isZero();
    }

    @Test
    void onUserConnectedEvent_incrementsConnectedUserCount() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getConnectedUserCount()).isEqualTo(1);
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onMultipleUserConnectedEvents_incrementsConnectedUserCount() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, ANOTHER_USER_ID_VALUE, ANOTHER_USERNAME_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getConnectedUserCount()).isEqualTo(2);
    }

    @Test
    void onUserDisconnectedEvent_decrementsConnectedUserCount() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
        eventBus.publish(new UserConnectedEvent(SESSION_ID_VALUE, ANOTHER_USER_ID_VALUE, ANOTHER_USERNAME_VALUE));

        eventBus.publish(new UserDisconnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getConnectedUserCount()).isEqualTo(1);
    }

    @Test
    void onEstimationStartedEvent_setsStatusToEstimating() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));

        eventBus.publish(new EstimationStartedEvent(SESSION_ID_VALUE, Instant.now().plusSeconds(30)));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(ESTIMATING);
    }

    @Test
    void onEstimationCompletedEvent_setsStatusToCompletedAndIncrementsCounter() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));

        eventBus.publish(new EstimationCompletedEvent(SESSION_ID_VALUE, "estimation-1", List.of("1", "2")));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(ESTIMATION_COMPLETED);
        assertThat(actual.getEstimationsCompleted()).isEqualTo(1);
    }

    @Test
    void onMultipleEstimationCompletedEvents_incrementsCounter() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));

        eventBus.publish(new EstimationCompletedEvent(SESSION_ID_VALUE, "estimation-1", List.of("1")));
        eventBus.publish(new EstimationStartedEvent(SESSION_ID_VALUE, null));
        eventBus.publish(new EstimationCompletedEvent(SESSION_ID_VALUE, "estimation-2", List.of("2")));
        eventBus.publish(new EstimationStartedEvent(SESSION_ID_VALUE, null));
        eventBus.publish(new EstimationCompletedEvent(SESSION_ID_VALUE, "estimation-3", List.of("3")));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getEstimationsCompleted()).isEqualTo(3);
    }

    @Test
    void onEstimateSetEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onEstimateClearedEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new EstimateClearedEvent(SESSION_ID_VALUE, USER_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onDeckSetEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new DeckSetEvent(SESSION_ID_VALUE, aDeckTO()));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onAutoRevealEnabledEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new AutoRevealEnabledEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onAutoJoinUpdatedEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new AutoJoinUpdatedEvent(SESSION_ID_VALUE, ESTIMATOR));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onTimerDurationSetEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new TimerDurationSetEvent(SESSION_ID_VALUE, THIRTY_SECONDS));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onReactionsEnabledEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new ReactionsEnabledEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }

    @Test
    void onUserRoleSetEvent_updatesLastActivity() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        var lastActivityBefore = repository.findById(SESSION_ID_VALUE).orElseThrow().getLastActivity();

        eventBus.publish(new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR));

        var actual = repository.findById(SESSION_ID_VALUE).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getLastActivity()).isAfterOrEqualTo(lastActivityBefore);
    }
}
