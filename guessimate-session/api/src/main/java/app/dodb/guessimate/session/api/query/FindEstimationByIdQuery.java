package app.dodb.guessimate.session.api.query;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.smd.api.query.Query;

import java.util.Optional;

public record FindEstimationByIdQuery(String sessionId, String estimationId) implements Query<Optional<EstimationTO>> {
}
