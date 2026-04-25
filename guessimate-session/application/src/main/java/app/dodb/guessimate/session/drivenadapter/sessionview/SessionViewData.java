package app.dodb.guessimate.session.drivenadapter.sessionview;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.api.event.EstimationRemovedEvent;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record SessionViewData(Set<EstimationTO> estimations) {

    SessionViewData apply(EstimationAddedEvent event) {
        var votesByEstimate = event.votesByEstimate();
        var insights = event.insights().stream()
            .map(Enum::toString)
            .collect(toSet());

        var updated = new HashSet<>(estimations);
        updated.add(new EstimationTO(event.estimationId(), event.timestamp(), event.deck(),
            event.estimates(), insights, event.amountOfParticipants(), votesByEstimate));
        return new SessionViewData(updated);
    }

    SessionViewData apply(EstimationRemovedEvent event) {
        var updated = estimations.stream()
            .filter(e -> !e.estimationId().equals(event.estimationId()))
            .collect(toSet());
        return new SessionViewData(updated);
    }

    public Set<EstimationTO> getEstimations() {
        return estimations;
    }
}
