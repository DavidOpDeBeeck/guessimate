package app.dodb.guessimate.session.usecase.stubs;

import app.dodb.guessimate.session.domain.Session;
import app.dodb.guessimate.session.domain.SessionId;
import app.dodb.guessimate.session.port.SessionRepository;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@SMDTestScope
public class SessionRepositoryStub implements SessionRepository {

    private final Map<SessionId, Session> sessionBySessionId = new ConcurrentHashMap<>();

    public void stubSession(SessionId sessionId, Session session) {
        sessionBySessionId.put(sessionId, session);
    }

    @Override
    public Optional<Session> findBy(SessionId sessionId) {
        return Optional.ofNullable(sessionBySessionId.get(sessionId));
    }
}
