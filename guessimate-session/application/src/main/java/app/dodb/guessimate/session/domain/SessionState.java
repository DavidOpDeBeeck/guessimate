package app.dodb.guessimate.session.domain;

import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.api.event.EstimationRemovedEvent;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.guessimate.session.domain.deck.Deck;

import java.util.HashSet;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

public class SessionState {

    private final SessionId sessionId;
    private final Set<Estimation> estimations;

    public SessionState(SessionId sessionId, Set<Estimation> estimations) {
        this.sessionId = requireNonNull(sessionId);
        this.estimations = requireNonNull(estimations);
    }

    void apply(SessionCreatedEvent event) {
        // do nothing
    }

    void apply(EstimationAddedEvent event) {
        var estimation = new Estimation(
            new EstimationId(event.estimationId()),
            event.timestamp(),
            event.estimates().stream()
                .map(Estimate::new)
                .toList(),
            Deck.from(event.deck())
        );
        estimations.add(estimation);
    }

    void apply(EstimationRemovedEvent event) {
        var estimationId = new EstimationId(event.estimationId());
        estimations.removeIf(estimation -> estimation.getEstimationId().equals(estimationId));
    }

    public Set<Estimation> getEstimations() {
        return new HashSet<>(estimations);
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public Estimation findOldestEstimation() {
        return estimations.stream()
            .min(comparing(Estimation::getTimestamp))
            .orElseThrow();
    }
}
