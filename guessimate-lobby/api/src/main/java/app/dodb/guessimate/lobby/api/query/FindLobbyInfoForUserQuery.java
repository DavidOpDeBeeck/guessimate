package app.dodb.guessimate.lobby.api.query;

import app.dodb.guessimate.lobby.api.event.LobbyInfo;
import app.dodb.smd.api.query.Query;

import java.util.Optional;

public record FindLobbyInfoForUserQuery(String sessionId, String userId) implements Query<Optional<LobbyInfo>> {
}
