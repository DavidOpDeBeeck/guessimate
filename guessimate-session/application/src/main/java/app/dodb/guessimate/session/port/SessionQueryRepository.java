package app.dodb.guessimate.session.port;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.api.query.FindEstimationByIdQuery;
import app.dodb.guessimate.session.api.query.FindEstimationsBySessionIdQuery;
import app.dodb.guessimate.session.api.query.FindSessionByIdQuery;

import java.util.Optional;
import java.util.Set;

public interface SessionQueryRepository {

    Optional<SessionTO> find(FindSessionByIdQuery query);

    Optional<Set<EstimationTO>> find(FindEstimationsBySessionIdQuery query);

    Optional<EstimationTO> find(FindEstimationByIdQuery query);
}
