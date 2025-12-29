package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.IntegrationTest;
import app.dodb.guessimate.lobby.api.LobbyMetricsTO;
import app.dodb.guessimate.lobby.api.query.FindLobbyMetricsQuery;
import app.dodb.guessimate.lobby.drivenadapter.lobbymetricsview.LobbyMetricsView;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.ANOTHER_SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindLobbyMetricsQueryIntegrationTest {

    @Inject
    QueryBus queryBus;
    @Inject
    EntityManager entityManager;

    @Test
    void whenNoLobbiesExist_returnsZeros() {
        var actual = queryBus.send(new FindLobbyMetricsQuery());

        assertThat(actual).isEqualTo(new LobbyMetricsTO(0, 0, 0));
    }

    @Test
    void whenLobbiesExist_returnsAggregatedMetrics() {
        entityManager.persist(new LobbyMetricsView.Builder()
            .sessionId(SESSION_ID_VALUE)
            .createdAt(Instant.now())
            .connectedUserCount(2)
            .estimationsCompleted(5)
            .build());
        entityManager.persist(new LobbyMetricsView.Builder()
            .sessionId(ANOTHER_SESSION_ID_VALUE)
            .createdAt(Instant.now())
            .connectedUserCount(3)
            .estimationsCompleted(3)
            .build());

        var actual = queryBus.send(new FindLobbyMetricsQuery());

        assertThat(actual).isEqualTo(new LobbyMetricsTO(2, 5, 8));
    }

    @Test
    void whenLobbyHasNoConnectedUsers_notCountedAsActive() {
        entityManager.persist(new LobbyMetricsView.Builder()
            .sessionId(SESSION_ID_VALUE)
            .createdAt(Instant.now())
            .connectedUserCount(2)
            .build());
        entityManager.persist(new LobbyMetricsView.Builder()
            .sessionId(ANOTHER_SESSION_ID_VALUE)
            .createdAt(Instant.now())
            .connectedUserCount(0)
            .build());

        var actual = queryBus.send(new FindLobbyMetricsQuery());

        assertThat(actual).isEqualTo(new LobbyMetricsTO(1, 2, 0));
    }
}
