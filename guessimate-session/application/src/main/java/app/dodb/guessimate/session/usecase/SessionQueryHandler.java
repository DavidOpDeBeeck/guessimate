package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.api.query.FindEstimationByIdQuery;
import app.dodb.guessimate.session.api.query.FindEstimationsBySessionIdQuery;
import app.dodb.guessimate.session.api.query.FindSessionByIdQuery;
import app.dodb.guessimate.session.port.SessionQueryRepository;
import app.dodb.smd.api.query.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class SessionQueryHandler {

    private final SessionQueryRepository sessionQueryRepository;

    public SessionQueryHandler(SessionQueryRepository sessionQueryRepository) {
        this.sessionQueryRepository = sessionQueryRepository;
    }

    @QueryHandler
    public Optional<SessionTO> handle(FindSessionByIdQuery query) {
        return sessionQueryRepository.find(query);
    }

    @QueryHandler
    public Optional<Set<EstimationTO>> handle(FindEstimationsBySessionIdQuery query) {
        return sessionQueryRepository.find(query);
    }

    @QueryHandler
    public Optional<EstimationTO> handle(FindEstimationByIdQuery query) {
        return sessionQueryRepository.find(query);
    }
}
