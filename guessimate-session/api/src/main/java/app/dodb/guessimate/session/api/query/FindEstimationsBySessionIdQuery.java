package app.dodb.guessimate.session.api.query;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.smd.api.query.Query;

import java.util.Optional;
import java.util.Set;

public record FindEstimationsBySessionIdQuery(String sessionId) implements Query<Optional<Set<EstimationTO>>> {
}
