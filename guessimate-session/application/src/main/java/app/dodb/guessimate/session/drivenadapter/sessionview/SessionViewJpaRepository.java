package app.dodb.guessimate.session.drivenadapter.sessionview;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.api.query.FindEstimationByIdQuery;
import app.dodb.guessimate.session.api.query.FindEstimationsBySessionIdQuery;
import app.dodb.guessimate.session.api.query.FindSessionByIdQuery;
import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import app.dodb.guessimate.session.domain.EstimationId;
import app.dodb.guessimate.session.domain.Session;
import app.dodb.guessimate.session.domain.SessionId;
import app.dodb.guessimate.session.domain.SessionState;
import app.dodb.guessimate.session.domain.deck.Deck;
import app.dodb.guessimate.session.port.SessionQueryRepository;
import app.dodb.guessimate.session.port.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Component
public class SessionViewJpaRepository implements SessionRepository, SessionQueryRepository {

    private final SessionViewSpringRepository repository;

    public SessionViewJpaRepository(SessionViewSpringRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SessionTO> find(FindSessionByIdQuery query) {
        return repository.findById(query.sessionId())
            .map(sessionView -> new SessionTO(sessionView.getSessionId()));
    }

    @Override
    public Optional<Set<EstimationTO>> find(FindEstimationsBySessionIdQuery query) {
        return repository.findById(query.sessionId())
            .map(SessionView::getEstimations)
            .map(estimations -> {
                TreeSet<EstimationTO> sorted = new TreeSet<>(comparing(EstimationTO::timestamp).reversed());
                sorted.addAll(estimations);
                return sorted;
            });
    }

    @Override
    public Optional<EstimationTO> find(FindEstimationByIdQuery query) {
        return repository.findById(query.sessionId())
            .flatMap(sessionView -> sessionView.getEstimations().stream()
                .filter(estimation -> estimation.estimationId().equals(query.estimationId()))
                .findFirst());
    }

    @Override
    public Optional<Session> findBy(SessionId sessionId) {
        return repository.findById(sessionId.value())
            .map(view -> new Session(new SessionState(new SessionId(view.getSessionId()), mapToEstimations(view))));
    }

    private Set<Estimation> mapToEstimations(SessionView view) {
        return view.getEstimations().stream()
            .map(estimation -> new Estimation(
                new EstimationId(estimation.estimationId()),
                estimation.timestamp(),
                estimation.estimates().stream()
                    .map(Estimate::new)
                    .collect(toList()),
                Deck.from(estimation.deck()))
            )
            .collect(Collectors.toSet());
    }
}
