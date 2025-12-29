package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetReactionCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionClearedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionSetEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.Emoji.THUMBS_UP;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetReactionCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void setsReaction() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new ReactionsEnabledEvent(SESSION_ID_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE),
            new EstimationCompletedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, List.of(ESTIMATE_VALUE))
        );

        send(new SetReactionCommand(THUMBS_UP));

        assertThat(smd.getEvents())
            .containsExactly(new ReactionSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, THUMBS_UP));
    }

    @Test
    void whenReactionsDisabled() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE),
            new EstimationCompletedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, List.of(ESTIMATE_VALUE))
        );

        send(new SetReactionCommand(THUMBS_UP));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void whenStatusIsEstimating() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new ReactionsEnabledEvent(SESSION_ID_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR)
        );

        send(new SetReactionCommand(THUMBS_UP));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void whenUserHasNoRole() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new ReactionsEnabledEvent(SESSION_ID_VALUE),
            new EstimationCompletedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, List.of(ESTIMATE_VALUE))
        );

        send(new SetReactionCommand(THUMBS_UP));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void togglesOff_whenSameReaction() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new ReactionsEnabledEvent(SESSION_ID_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR),
            new EstimationCompletedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, List.of(ESTIMATE_VALUE)),
            new ReactionSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, THUMBS_UP)
        );

        send(new SetReactionCommand(THUMBS_UP));

        assertThat(smd.getEvents())
            .containsExactly(new ReactionClearedEvent(SESSION_ID_VALUE, USER_ID_VALUE));
    }

    private void send(SetReactionCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
