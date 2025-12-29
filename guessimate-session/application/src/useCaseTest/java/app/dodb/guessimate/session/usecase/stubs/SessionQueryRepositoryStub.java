package app.dodb.guessimate.session.usecase.stubs;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.api.query.FindEstimationByIdQuery;
import app.dodb.guessimate.session.api.query.FindEstimationsBySessionIdQuery;
import app.dodb.guessimate.session.api.query.FindSessionByIdQuery;
import app.dodb.guessimate.session.port.SessionQueryRepository;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@SMDTestScope
public class SessionQueryRepositoryStub implements SessionQueryRepository {

    @Override
    public Optional<SessionTO> find(FindSessionByIdQuery query) {
        return Optional.empty();
    }

    @Override
    public Optional<Set<EstimationTO>> find(FindEstimationsBySessionIdQuery query) {
        return Optional.empty();
    }

    @Override
    public Optional<EstimationTO> find(FindEstimationByIdQuery query) {
        return Optional.empty();
    }
}
