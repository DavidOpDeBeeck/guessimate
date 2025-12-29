package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetUserRoleCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.lobby.api.event.UserRole.OBSERVER;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetUserRoleCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void toEstimator() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetUserRoleCommand(ESTIMATOR));

        assertThat(smd.getEvents())
            .containsExactly(new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR));
    }

    @Test
    void toObserver() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetUserRoleCommand(OBSERVER));

        assertThat(smd.getEvents())
            .containsExactly(new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, OBSERVER));
    }

    @Test
    void toEstimator_whenAlreadyEstimator() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR)
        );

        send(new SetUserRoleCommand(ESTIMATOR));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void toObserver_whenAlreadyObserver() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, OBSERVER)
        );

        send(new SetUserRoleCommand(OBSERVER));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void toObserver_withExistingEstimate() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE)
        );

        send(new SetUserRoleCommand(OBSERVER));

        assertThat(smd.getEvents())
            .containsExactly(
                new EstimateClearedEvent(SESSION_ID_VALUE, USER_ID_VALUE),
                new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, OBSERVER));
    }

    @Test
    void toEstimator_whenObserver() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, OBSERVER)
        );

        send(new SetUserRoleCommand(ESTIMATOR));

        assertThat(smd.getEvents())
            .containsExactly(new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR));
    }

    private void send(SetUserRoleCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
