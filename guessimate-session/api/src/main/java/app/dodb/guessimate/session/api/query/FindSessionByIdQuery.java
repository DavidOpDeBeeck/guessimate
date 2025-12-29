package app.dodb.guessimate.session.api.query;

import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.smd.api.query.Query;

import java.util.Optional;

public record FindSessionByIdQuery(String sessionId) implements Query<Optional<SessionTO>> {
}
