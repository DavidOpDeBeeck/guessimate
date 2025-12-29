package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetEstimateCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
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
import static app.dodb.guessimate.lobby.api.event.UserRole.OBSERVER;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_NOT_IN_DECK_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetEstimateCommandHandlerTest extends CommandHandlerTestBase {

    @BeforeEach
    void setUp() {
        smd.stubCommand(new AddEstimationCommand(SESSION_ID_VALUE, aDeckTO(), List.of(ESTIMATE_VALUE)), ESTIMATION_ID_VALUE);
    }

    @Test
    void setsEstimate() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetEstimateCommand(ESTIMATE_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE));
    }

    @Test
    void withAutoRevealEnabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new AutoRevealEnabledEvent(SESSION_ID_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR)
        );

        send(new SetEstimateCommand(ESTIMATE_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(
                new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE),
                new EstimationCompletedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, List.of(ESTIMATE_VALUE)));
    }

    @Test
    void whenEstimateNotInDeck() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetEstimateCommand(ESTIMATE_NOT_IN_DECK_VALUE));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void asObserver() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, OBSERVER)
        );

        send(new SetEstimateCommand(ESTIMATE_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE));
    }

    @Test
    void whenSameEstimate() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE)
        );

        send(new SetEstimateCommand(ESTIMATE_VALUE));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void afterClearingEstimate() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE),
            new EstimateClearedEvent(SESSION_ID_VALUE, USER_ID_VALUE)
        );

        send(new SetEstimateCommand(ESTIMATE_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE));
    }

    @Test
    void afterCompletingEstimation() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE),
            new EstimationCompletedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, List.of(ESTIMATE_VALUE))
        );

        send(new SetEstimateCommand(ESTIMATE_VALUE));

        assertThat(smd.getEvents()).isEmpty();
    }

    private void send(SetEstimateCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
