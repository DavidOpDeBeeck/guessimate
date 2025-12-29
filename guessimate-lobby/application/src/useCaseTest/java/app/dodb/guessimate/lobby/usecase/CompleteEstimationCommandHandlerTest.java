package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.CompleteEstimationCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.session.api.command.AddEstimationCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class CompleteEstimationCommandHandlerTest extends CommandHandlerTestBase {

    @BeforeEach
    void setUp() {
        smd.stubCommand(new AddEstimationCommand(SESSION_ID_VALUE, aDeckTO(), List.of(ESTIMATE_VALUE)), ESTIMATION_ID_VALUE);
    }

    @Test
    void manually() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE)
        );

        send(new CompleteEstimationCommand());

        assertThat(smd.getEvents())
            .hasSize(1)
            .first()
            .isInstanceOf(EstimationCompletedEvent.class);
    }

    @Test
    void whenNoEstimates() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR)
        );

        send(new CompleteEstimationCommand());

        assertThat(smd.getEvents()).isEmpty();
    }

    private void send(CompleteEstimationCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
