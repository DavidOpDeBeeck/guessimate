package app.dodb.guessimate.session.domain;

import app.dodb.guessimate.session.domain.deck.Deck;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Estimation {

    private final EstimationId estimationId;
    private final LocalDateTime timestamp;
    private final List<Estimate> estimates;
    private final Deck deck;

    public Estimation(EstimationId estimationId, LocalDateTime timestamp, List<Estimate> estimates, Deck deck) {
        this.estimationId = requireNonNull(estimationId);
        this.timestamp = requireNonNull(timestamp);
        this.estimates = requireNonNull(estimates);
        this.deck = requireNonNull(deck);
    }

    public boolean isConsensus() {
        long uniqueEstimates = estimates.stream()
            .distinct()
            .count();
        return uniqueEstimates == 1;
    }

    public EstimationId getEstimationId() {
        return estimationId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<Estimate> getEstimates() {
        return estimates;
    }

    public Deck getDeck() {
        return deck;
    }

    public Map<Estimate, Integer> deckIndexByEstimate() {
        var deckIndexByEstimate = new LinkedHashMap<Estimate, Integer>();
        for (Estimate estimate : estimates) {
            deckIndexByEstimate.putIfAbsent(estimate, deck.indexOf(estimate));
        }
        return deckIndexByEstimate;
    }

    public Map<Estimate, Long> votesByEstimate() {
        return estimates.stream()
            .collect(groupingBy(identity(), LinkedHashMap::new, counting()));
    }
}
