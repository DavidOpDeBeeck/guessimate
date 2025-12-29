package app.dodb.guessimate.lobby.port;

import app.dodb.guessimate.lobby.domain.Lobby;
import app.dodb.guessimate.lobby.domain.SessionId;

import java.util.Optional;

public interface LobbyRepository {

    Optional<Lobby> findBySessionId(SessionId sessionId);
}
