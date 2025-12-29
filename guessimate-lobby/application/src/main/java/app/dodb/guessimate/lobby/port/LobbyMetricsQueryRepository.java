package app.dodb.guessimate.lobby.port;

import app.dodb.guessimate.lobby.api.LobbyMetricsTO;
import app.dodb.guessimate.lobby.api.query.FindLobbyMetricsQuery;

public interface LobbyMetricsQueryRepository {

    LobbyMetricsTO find(FindLobbyMetricsQuery query);
}
