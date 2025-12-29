package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.IntegrationTest;
import app.dodb.guessimate.lobby.api.event.LobbyConfiguration;
import app.dodb.guessimate.lobby.api.event.LobbyInfo;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
import app.dodb.guessimate.lobby.api.event.UserInfo;
import app.dodb.guessimate.lobby.api.query.FindLobbyInfoForUserQuery;
import app.dodb.guessimate.lobby.drivenadapter.lobbyview.LobbyData;
import app.dodb.guessimate.lobby.drivenadapter.lobbyview.LobbyView;
import app.dodb.guessimate.lobby.drivenadapter.lobbyview.UserData;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.ANOTHER_USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.ANOTHER_USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.Emoji.THUMBS_UP;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.TimerDuration.FIVE_SECONDS;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.NON_EXISTENT_SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindLobbyInfoForUserQueryIntegrationTest {

    @Inject
    QueryBus queryBus;
    @Inject
    EntityManager entityManager;

    @Test
    void whenLobbyExists_thenReturnLobbyInfo() {
        Instant timerExpiresAt = Instant.now();
        var lobbyView = new LobbyView(SESSION_ID_VALUE, new LobbyData.Builder()
            .deck(aDeckTO())
            .autoReveal(true)
            .autoJoinRole(ESTIMATOR)
            .timerDuration(FIVE_SECONDS)
            .reactionsEnabled(true)
            .previousEstimationId(ESTIMATION_ID_VALUE)
            .timerExpiresAt(timerExpiresAt)
            .status(LobbyStatus.ESTIMATING)
            .users(List.of(
                new UserData.Builder()
                    .userId(USER_ID_VALUE)
                    .username(USERNAME_VALUE)
                    .role(ESTIMATOR)
                    .estimate(ESTIMATE_VALUE)
                    .reaction(THUMBS_UP)
                    .build(),
                new UserData.Builder()
                    .userId(ANOTHER_USER_ID_VALUE)
                    .username(ANOTHER_USERNAME_VALUE)
                    .build()))
            .build());
        entityManager.persist(lobbyView);

        var actual = queryBus.send(new FindLobbyInfoForUserQuery(SESSION_ID_VALUE, USER_ID_VALUE));

        assertThat(actual).contains(new LobbyInfo(
            SESSION_ID_VALUE,
            new LobbyConfiguration(aDeckTO(), true, ESTIMATOR, FIVE_SECONDS, true),
            ESTIMATION_ID_VALUE,
            ESTIMATING,
            List.of(
                new UserInfo(USER_ID_VALUE, USERNAME_VALUE, ESTIMATE_VALUE, THUMBS_UP, ESTIMATOR, true),
                new UserInfo(ANOTHER_USER_ID_VALUE, ANOTHER_USERNAME_VALUE, null, null, null, false)
            ),
            timerExpiresAt.toString()
        ));
    }

    @Test
    void whenLobbyDoesNotExists_thenReturnEmpty() {
        var actual = queryBus.send(new FindLobbyInfoForUserQuery(NON_EXISTENT_SESSION_ID_VALUE, USER_ID_VALUE));

        assertThat(actual).isEmpty();
    }
}
