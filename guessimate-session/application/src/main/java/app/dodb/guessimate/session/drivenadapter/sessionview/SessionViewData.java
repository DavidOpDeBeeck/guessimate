package app.dodb.guessimate.session.drivenadapter.sessionview;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.api.event.EstimationRemovedEvent;

import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class SessionViewData {

    private Set<EstimationTO> estimations;

    public SessionViewData() {
    }

    public SessionViewData(Set<EstimationTO> estimations) {
        this.estimations = requireNonNull(estimations);
    }

    void apply(EstimationAddedEvent event) {
        var votesByEstimate = event.votesByEstimate();
        var insights = event.insights().stream()
            .map(Enum::toString)
            .collect(toSet());

        estimations.add(new EstimationTO(event.estimationId(), event.timestamp(), event.deck(), event.estimates(), insights, event.amountOfParticipants(), votesByEstimate));
    }

    void apply(EstimationRemovedEvent event) {
        estimations.removeIf(estimation -> estimation.estimationId().equals(event.estimationId()));
    }

    public Set<EstimationTO> getEstimations() {
        return estimations;
    }
}
