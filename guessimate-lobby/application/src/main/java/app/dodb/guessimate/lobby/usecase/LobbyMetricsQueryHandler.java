package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.LobbyMetricsTO;
import app.dodb.guessimate.lobby.api.query.FindLobbyMetricsQuery;
import app.dodb.guessimate.lobby.port.LobbyMetricsQueryRepository;
import app.dodb.smd.api.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class LobbyMetricsQueryHandler {

    private final LobbyMetricsQueryRepository lobbyMetricsQueryRepository;

    public LobbyMetricsQueryHandler(LobbyMetricsQueryRepository lobbyMetricsQueryRepository) {
        this.lobbyMetricsQueryRepository = lobbyMetricsQueryRepository;
    }

    @QueryHandler
    public LobbyMetricsTO handle(FindLobbyMetricsQuery query) {
        return lobbyMetricsQueryRepository.find(query);
    }
}
