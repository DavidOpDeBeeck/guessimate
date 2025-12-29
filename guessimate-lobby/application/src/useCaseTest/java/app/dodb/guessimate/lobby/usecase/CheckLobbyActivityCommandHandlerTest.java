package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.CheckLobbyActivityCommand;
import app.dodb.guessimate.lobby.api.event.KeepAliveEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.domain.UserId;
import app.dodb.guessimate.lobby.usecase.stubs.UserConnectivityCheckerStub;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class CheckLobbyActivityCommandHandlerTest extends CommandHandlerTestBase {

    @Inject
    private UserConnectivityCheckerStub userConnectivityPort;

    @Test
    void disconnectsInactiveUsers() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        smd.send(new CheckLobbyActivityCommand(SESSION_ID_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(
                new UserDisconnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE),
                new KeepAliveEvent(SESSION_ID_VALUE));
    }

    @Test
    void keepsActiveUsers_andSendsKeepAlive() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );
        userConnectivityPort.setConnected(new UserId(USER_ID_VALUE), true);

        smd.send(new CheckLobbyActivityCommand(SESSION_ID_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new KeepAliveEvent(SESSION_ID_VALUE));
    }
}
