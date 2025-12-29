package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.IntegrationTest;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
import app.dodb.guessimate.lobby.api.event.TimerDuration;
import app.dodb.guessimate.lobby.api.query.FindActiveLobbiesQuery;
import app.dodb.guessimate.lobby.drivenadapter.lobbyview.LobbyData;
import app.dodb.guessimate.lobby.drivenadapter.lobbyview.LobbyView;
import app.dodb.guessimate.lobby.drivenadapter.lobbyview.UserData;
import app.dodb.guessimate.session.api.SessionApiTestConstants;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindActiveLobbiesQueryIntegrationTest {

    @Inject
    QueryBus queryBus;
    @Inject
    EntityManager entityManager;

    @Test
    void whenLobbyHasUsers_returnsSessionId() {
        var lobbyData = new LobbyData.Builder()
            .deck(SessionApiTestConstants.aDeckTO())
            .autoReveal(false)
            .autoJoinRole(null)
            .timerDuration(TimerDuration.DISABLED)
            .reactionsEnabled(false)
            .previousEstimationId(null)
            .timerExpiresAt(null)
            .status(LobbyStatus.ESTIMATING)
            .users(List.of(
                new UserData.Builder()
                    .userId(USER_ID_VALUE)
                    .username(USERNAME_VALUE)
                    .build()
            ))
            .build();
        entityManager.persist(new LobbyView(SESSION_ID_VALUE, lobbyData));

        var actual = queryBus.send(new FindActiveLobbiesQuery());

        assertThat(actual).contains(SESSION_ID_VALUE);
    }

    @Test
    void whenLobbyHasNoUsers_doesNotReturnSessionId() {
        entityManager.persist(new LobbyView(SESSION_ID_VALUE, new LobbyData.Builder()
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

        var actual = queryBus.send(new FindActiveLobbiesQuery());

        assertThat(actual).doesNotContain(SESSION_ID_VALUE);
    }
}
