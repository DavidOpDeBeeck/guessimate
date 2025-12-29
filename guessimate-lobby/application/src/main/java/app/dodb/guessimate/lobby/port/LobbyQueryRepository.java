package app.dodb.guessimate.lobby.port;

import app.dodb.guessimate.lobby.api.event.LobbyInfo;
import app.dodb.guessimate.lobby.api.query.FindActiveLobbiesQuery;
import app.dodb.guessimate.lobby.api.query.FindLobbyInfoForUserQuery;

import java.util.List;
import java.util.Optional;

public interface LobbyQueryRepository {

    List<String> find(FindActiveLobbiesQuery query);

    Optional<LobbyInfo> find(FindLobbyInfoForUserQuery query);
}
