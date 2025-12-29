package app.dodb.guessimate.lobby.usecase.stubs;

import app.dodb.guessimate.lobby.domain.Lobby;
import app.dodb.guessimate.lobby.domain.SessionId;
import app.dodb.guessimate.lobby.port.LobbyRepository;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@SMDTestScope
public class LobbyRepositoryStub implements LobbyRepository {

    private final Map<SessionId, Lobby> lobbyBySessionId = new ConcurrentHashMap<>();

    public void stubLobby(SessionId sessionId, Lobby lobby) {
        lobbyBySessionId.put(sessionId, lobby);
    }

    @Override
    public Optional<Lobby> findBySessionId(SessionId sessionId) {
        return Optional.ofNullable(lobbyBySessionId.get(sessionId));
    }
}
