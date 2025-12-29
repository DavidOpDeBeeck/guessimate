package app.dodb.guessimate.lobby.usecase.stubs;

import app.dodb.guessimate.lobby.api.event.LobbyInfo;
import app.dodb.guessimate.lobby.api.query.FindActiveLobbiesQuery;
import app.dodb.guessimate.lobby.api.query.FindLobbyInfoForUserQuery;
import app.dodb.guessimate.lobby.port.LobbyQueryRepository;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@SMDTestScope
public class LobbyQueryRepositoryStub implements LobbyQueryRepository {

    @Override
    public List<String> find(FindActiveLobbiesQuery query) {
        return List.of();
    }

    @Override
    public Optional<LobbyInfo> find(FindLobbyInfoForUserQuery query) {
        return Optional.empty();
    }
}
