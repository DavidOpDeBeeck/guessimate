package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.event.LobbyInfo;
import app.dodb.guessimate.lobby.api.query.FindActiveLobbiesQuery;
import app.dodb.guessimate.lobby.api.query.FindLobbyInfoForUserQuery;
import app.dodb.guessimate.lobby.port.LobbyQueryRepository;
import app.dodb.smd.api.query.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LobbyQueryHandler {

    private final LobbyQueryRepository lobbyQueryRepository;

    public LobbyQueryHandler(LobbyQueryRepository lobbyQueryRepository) {
        this.lobbyQueryRepository = lobbyQueryRepository;
    }

    @QueryHandler
    public List<String> handle(FindActiveLobbiesQuery query) {
        return lobbyQueryRepository.find(query);
    }

    @QueryHandler
    public Optional<LobbyInfo> handle(FindLobbyInfoForUserQuery query) {
        return lobbyQueryRepository.find(query);
    }
}
