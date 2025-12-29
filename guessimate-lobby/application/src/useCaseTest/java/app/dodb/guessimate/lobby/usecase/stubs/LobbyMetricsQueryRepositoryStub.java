package app.dodb.guessimate.lobby.usecase.stubs;

import app.dodb.guessimate.lobby.api.LobbyMetricsTO;
import app.dodb.guessimate.lobby.api.query.FindLobbyMetricsQuery;
import app.dodb.guessimate.lobby.port.LobbyMetricsQueryRepository;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

@Component
@SMDTestScope
public class LobbyMetricsQueryRepositoryStub implements LobbyMetricsQueryRepository {

    @Override
    public LobbyMetricsTO find(FindLobbyMetricsQuery query) {
        return new LobbyMetricsTO(0L, 0L, 0L);
    }
}
