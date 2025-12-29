package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.event.LobbyEvent;
import app.dodb.guessimate.lobby.domain.Lobby;
import app.dodb.guessimate.lobby.domain.LobbyState;
import app.dodb.guessimate.lobby.usecase.stubs.LobbyRepositoryStub;
import app.dodb.smd.test.SMDTestExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.TimerDuration.DISABLED;
import static app.dodb.guessimate.lobby.usecase.LobbyTestConstants.SESSION_ID;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static java.util.Arrays.stream;

public abstract class CommandHandlerTestBase {

    protected static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    @Inject
    protected SMDTestExtension smd;
    @Inject
    protected LobbyRepositoryStub lobbyRepository;

    @BeforeEach
    void setUpBase() {
        smd.stubTimestamp(TIMESTAMP);
    }

    protected void given(LobbyEvent... events) {
        var lobbyState = new LobbyState(SESSION_ID, aDeckTO(), false, null, DISABLED, false, null, null, ESTIMATING, new ArrayList<>());
        stream(events).forEach(lobbyState::apply);
        lobbyRepository.stubLobby(SESSION_ID, new Lobby(lobbyState));
    }
}
