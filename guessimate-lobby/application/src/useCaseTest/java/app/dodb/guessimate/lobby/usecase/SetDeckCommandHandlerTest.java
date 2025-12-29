package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.SetDeckCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.session.api.query.FindDeckByNameQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ANOTHER_DECK_NAME_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.DECK_NAME_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.NON_EXISTENT_DECK_NAME_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.anotherDeckTO;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class SetDeckCommandHandlerTest extends CommandHandlerTestBase {

    @BeforeEach
    void setUp() {
        smd.stubQuery(new FindDeckByNameQuery(DECK_NAME_VALUE), Optional.of(aDeckTO()));
        smd.stubQuery(new FindDeckByNameQuery(ANOTHER_DECK_NAME_VALUE), Optional.of(anotherDeckTO()));
        smd.stubQuery(new FindDeckByNameQuery(NON_EXISTENT_DECK_NAME_VALUE), Optional.empty());
    }

    @Test
    void setsDeck() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetDeckCommand(ANOTHER_DECK_NAME_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new DeckSetEvent(SESSION_ID_VALUE, anotherDeckTO()));
    }

    @Test
    void whenSameDeck() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetDeckCommand(DECK_NAME_VALUE));

        assertThat(smd.getEvents()).isEmpty();
    }

    @Test
    void clearsEstimates() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
            new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR),
            new EstimateSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATE_VALUE)
        );

        send(new SetDeckCommand(ANOTHER_DECK_NAME_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(
                new EstimateClearedEvent(SESSION_ID_VALUE, USER_ID_VALUE),
                new DeckSetEvent(SESSION_ID_VALUE, anotherDeckTO()));
    }

    @Test
    void whenDeckNotFound() {
        given(
            new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE)
        );

        send(new SetDeckCommand(NON_EXISTENT_DECK_NAME_VALUE));

        assertThat(smd.getEvents()).isEmpty();
    }

    private void send(SetDeckCommand command) {
        smd.send(new UserActionCommand<>(SESSION_ID_VALUE, USER_ID_VALUE, command));
    }
}
