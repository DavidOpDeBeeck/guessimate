package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetAutoRevealCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetAutoRevealCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void enabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetAutoRevealCommand(true));

        assertThat(smd.getEvents())
            .containsExactly(new AutoRevealEnabledEvent(SESSION_ID_VALUE));
    }

    @Test
    void enabledTwice() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new AutoRevealEnabledEvent(SESSION_ID_VALUE)
        );

        send(new SetAutoRevealCommand(true));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void disabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetAutoRevealCommand(false));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void disabled_whenPreviouslyEnabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new AutoRevealEnabledEvent(SESSION_ID_VALUE)
        );

        send(new SetAutoRevealCommand(false));

        assertThat(smd.getEvents())
            .containsExactly(new AutoRevealDisabledEvent(SESSION_ID_VALUE));
    }

    @Test
    void disabledTwice() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new AutoRevealDisabledEvent(SESSION_ID_VALUE)
        );

        send(new SetAutoRevealCommand(false));

        assertThat(smd.getEvents()).isEmpty();
    }

    private void send(SetAutoRevealCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
