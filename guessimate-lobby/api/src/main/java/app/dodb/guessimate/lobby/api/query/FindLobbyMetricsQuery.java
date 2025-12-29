package app.dodb.guessimate.lobby.api.query;

import app.dodb.guessimate.lobby.api.LobbyMetricsTO;
import app.dodb.smd.api.query.Query;

public record FindLobbyMetricsQuery() implements Query<LobbyMetricsTO> {
}
