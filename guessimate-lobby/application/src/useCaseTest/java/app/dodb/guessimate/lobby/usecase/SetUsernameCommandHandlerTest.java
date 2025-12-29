package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetUsernameCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UsernameSetEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.ANOTHER_USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetUsernameCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void setsUsername() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetUsernameCommand(ANOTHER_USERNAME_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new UsernameSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ANOTHER_USERNAME_VALUE));
    }

    @Test
    void whenSameUsername() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetUsernameCommand(USERNAME_VALUE));

        assertThat(smd.getEvents()).isEmpty();
    }

    private void send(SetUsernameCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
