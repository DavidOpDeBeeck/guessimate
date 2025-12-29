package app.dodb.guessimate.session.api.event;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.api.deck.DeckTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public record EstimationAddedEvent(String sessionId,
                                   String estimationId,
                                   LocalDateTime timestamp,
                                   DeckTO deck,
                                   List<String> estimates,
                                   Set<EstimationInsight> insights) implements SessionEvent {

    public int amountOfParticipants() {
        return estimates.size();
    }

    public Map<String, Long> votesByEstimate() {
        return estimates.stream()
            .collect(groupingBy(identity(), counting()));
    }
}
