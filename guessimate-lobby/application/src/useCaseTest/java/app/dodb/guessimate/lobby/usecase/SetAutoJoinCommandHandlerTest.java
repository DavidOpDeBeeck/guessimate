package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetAutoJoinCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetAutoJoinCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void setsAutoJoin() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetAutoJoinCommand(ESTIMATOR));

        assertThat(smd.getEvents())
            .containsExactly(new AutoJoinUpdatedEvent(SESSION_ID_VALUE, ESTIMATOR));
    }

    @Test
    void whenSameAutoJoin() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new AutoJoinUpdatedEvent(SESSION_ID_VALUE, ESTIMATOR)
        );

        send(new SetAutoJoinCommand(ESTIMATOR));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void toNull() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new AutoJoinUpdatedEvent(SESSION_ID_VALUE, ESTIMATOR)
        );

        send(new SetAutoJoinCommand(null));

        assertThat(smd.getEvents())
            .containsExactly(new AutoJoinUpdatedEvent(SESSION_ID_VALUE, null));
    }

    private void send(SetAutoJoinCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
