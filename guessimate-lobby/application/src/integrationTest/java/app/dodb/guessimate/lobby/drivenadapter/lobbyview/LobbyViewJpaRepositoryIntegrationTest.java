package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.IntegrationTest;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
import app.dodb.guessimate.lobby.api.event.TimerDuration;
import app.dodb.guessimate.lobby.domain.SessionId;
import app.dodb.guessimate.session.api.SessionApiTestConstants;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.NON_EXISTENT_SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class LobbyViewJpaRepositoryIntegrationTest {

    private static final SessionId NON_EXISTENT_SESSION_ID = new SessionId(NON_EXISTENT_SESSION_ID_VALUE);

    @Inject
    EntityManager entityManager;
    @Inject
    LobbyViewJpaRepository repository;

    @Test
    void findBySessionId_whenLobbyExists_returnsLobby() {
        var sessionId = new SessionId(UUID.randomUUID().toString());
        entityManager.persist(new LobbyView(sessionId.value(), new LobbyData.Builder()
            .deck(SessionApiTestConstants.aDeckTO())
            .autoReveal(false)
            .autoJoinRole(null)
            .timerDuration(TimerDuration.DISABLED)
            .reactionsEnabled(false)
            .previousEstimationId(null)
            .timerExpiresAt(null)
            .status(LobbyStatus.ESTIMATING)
            .users(new ArrayList<>())
            .build()));

        var actual = repository.findBySessionId(sessionId);

        assertThat(actual).isPresent();
    }

    @Test
    void findBySessionId_whenLobbyDoesNotExist_returnsEmpty() {
        var actual = repository.findBySessionId(NON_EXISTENT_SESSION_ID);

        assertThat(actual).isEmpty();
    }
}
