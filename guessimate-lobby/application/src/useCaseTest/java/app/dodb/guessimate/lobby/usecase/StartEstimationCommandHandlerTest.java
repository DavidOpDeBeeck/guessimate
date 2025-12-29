package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.StartEstimationCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class StartEstimationCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void startsEstimation() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new StartEstimationCommand());

        assertThat(smd.getEvents())
            .hasSize(1)
            .first()
            .isInstanceOf(EstimationStartedEvent.class);
    }

    @Test
    void afterEstimationAlreadyStarted() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new EstimationStartedEvent(SESSION_ID_VALUE, null)
        );

        send(new StartEstimationCommand());

        assertThat(smd.getEvents())
            .containsExactly(new EstimationStartedEvent(SESSION_ID_VALUE, null));
    }

    private void send(StartEstimationCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
