package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetTimerDurationCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.TimerDuration.DISABLED;
import static app.dodb.guessimate.lobby.api.event.TimerDuration.FIVE_SECONDS;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetTimerDurationCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void setsTimerDuration() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetTimerDurationCommand(FIVE_SECONDS));

        assertThat(smd.getEvents())
            .containsExactly(
                new TimerDurationSetEvent(SESSION_ID_VALUE, FIVE_SECONDS),
                new EstimationStartedEvent(SESSION_ID_VALUE, TIMESTAMP.plusSeconds(5).atZone(systemDefault()).toInstant()));
    }

    @Test
    void whenSameDuration() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new TimerDurationSetEvent(SESSION_ID_VALUE, FIVE_SECONDS)
        );

        send(new SetTimerDurationCommand(FIVE_SECONDS));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void toDisabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new TimerDurationSetEvent(SESSION_ID_VALUE, FIVE_SECONDS)
        );

        send(new SetTimerDurationCommand(DISABLED));

        assertThat(smd.getEvents())
            .containsExactly(
                new TimerDurationSetEvent(SESSION_ID_VALUE, DISABLED),
                new EstimationStartedEvent(SESSION_ID_VALUE, null));
    }

    private void send(SetTimerDurationCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
