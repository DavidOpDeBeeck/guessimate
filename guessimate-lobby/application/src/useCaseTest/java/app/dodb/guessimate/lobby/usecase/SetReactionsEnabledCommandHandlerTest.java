package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetReactionsEnabledCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.ReactionsDisabledEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetReactionsEnabledCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void enabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetReactionsEnabledCommand(true));

        assertThat(smd.getEvents())
            .containsExactly(new ReactionsEnabledEvent(SESSION_ID_VALUE));
    }

    @Test
    void disabled_whenPreviouslyEnabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new ReactionsEnabledEvent(SESSION_ID_VALUE)
        );

        send(new SetReactionsEnabledCommand(false));

        assertThat(smd.getEvents())
            .containsExactly(new ReactionsDisabledEvent(SESSION_ID_VALUE));
    }

    @Test
    void enabled_whenAlreadyEnabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new ReactionsEnabledEvent(SESSION_ID_VALUE)
        );

        send(new SetReactionsEnabledCommand(true));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void disabled_whenAlreadyDisabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetReactionsEnabledCommand(false));

        assertThat(smd.getEvents()).isEmpty();
    }

    private void send(SetReactionsEnabledCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
