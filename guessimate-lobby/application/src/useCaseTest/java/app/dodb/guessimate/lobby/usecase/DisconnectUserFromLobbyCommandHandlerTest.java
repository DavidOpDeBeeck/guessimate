package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.DisconnectUserFromLobbyCommand;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class DisconnectUserFromLobbyCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void disconnectsUser() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        smd.send(new DisconnectUserFromLobbyCommand(SESSION_ID_VALUE, USER_ID_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new UserDisconnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE));
    }

    @Test
    void disconnectsUser_withEstimate() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE)
        );

        smd.send(new DisconnectUserFromLobbyCommand(SESSION_ID_VALUE, USER_ID_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new UserDisconnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE));
    }
}
