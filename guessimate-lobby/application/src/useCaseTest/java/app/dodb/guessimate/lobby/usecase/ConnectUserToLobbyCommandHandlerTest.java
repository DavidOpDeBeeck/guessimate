package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.ConnectUserToLobbyCommand;
import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.lobby.usecase.stubs.UsernameGeneratorStub;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.ANOTHER_USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USERNAME_VALUE;
import static app.dodb.guessimate.lobby.api.LobbyApiTestConstants.USER_ID_VALUE;
import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.lobby.api.event.UserRole.OBSERVER;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class ConnectUserToLobbyCommandHandlerTest extends CommandHandlerTestBase {

    @Inject
    protected UsernameGeneratorStub usernameGenerator;

    @BeforeEach
    void setUp() {
        usernameGenerator.stubNextUsername(USERNAME_VALUE);
    }

    @Test
    void withUsername() {
        given();

        smd.send(new ConnectUserToLobbyCommand(SESSION_ID_VALUE, USER_ID_VALUE, Optional.of(ANOTHER_USERNAME_VALUE)));

        assertThat(smd.getEvents())
            .containsExactly(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, ANOTHER_USERNAME_VALUE));
    }

    @Test
    void withoutUsername() {
        given();

        smd.send(new ConnectUserToLobbyCommand(SESSION_ID_VALUE, USER_ID_VALUE, Optional.empty()));

        assertThat(smd.getEvents())
            .containsExactly(new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE));
    }

    @Test
    void withAutoJoinEstimator() {
        given(
            new AutoJoinUpdatedEvent(SESSION_ID_VALUE, ESTIMATOR)
        );

        smd.send(new ConnectUserToLobbyCommand(SESSION_ID_VALUE, USER_ID_VALUE, Optional.empty()));

        assertThat(smd.getEvents())
            .containsExactly(
                new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
                new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, ESTIMATOR));
    }

    @Test
    void withAutoJoinObserver() {
        given(
            new AutoJoinUpdatedEvent(SESSION_ID_VALUE, OBSERVER)
        );

        smd.send(new ConnectUserToLobbyCommand(SESSION_ID_VALUE, USER_ID_VALUE, Optional.empty()));

        assertThat(smd.getEvents())
            .containsExactly(
                new UserConnectedEvent(SESSION_ID_VALUE, USER_ID_VALUE, USERNAME_VALUE),
                new UserRoleSetEvent(SESSION_ID_VALUE, USER_ID_VALUE, OBSERVER));
    }
}
