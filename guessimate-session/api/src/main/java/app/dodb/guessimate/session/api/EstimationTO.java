package app.dodb.guessimate.session.api;

import app.dodb.guessimate.session.api.deck.DeckTO;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record EstimationTO(String estimationId,
                           Instant timestamp,
                           DeckTO deck,
                           List<String> estimates,
                           Set<String> insights,
                           int amountOfParticipants,
                           Map<String, Long> votesByEstimate) {
}
