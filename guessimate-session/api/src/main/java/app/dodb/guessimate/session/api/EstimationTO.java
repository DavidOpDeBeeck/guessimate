package app.dodb.guessimate.session.api;

import app.dodb.guessimate.session.api.deck.DeckTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record EstimationTO(String estimationId,
                           LocalDateTime timestamp,
                           DeckTO deck,
                           List<String> estimates,
                           Set<String> insights,
                           int amountOfParticipants,
                           Map<String, Long> votesByEstimate) {
}
