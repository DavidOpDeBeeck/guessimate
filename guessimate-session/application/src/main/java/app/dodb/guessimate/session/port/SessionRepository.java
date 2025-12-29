package app.dodb.guessimate.session.port;

import app.dodb.guessimate.session.domain.Session;
import app.dodb.guessimate.session.domain.SessionId;

import java.util.Optional;

public interface SessionRepository {

    Optional<Session> findBy(SessionId sessionId);
}
