package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.HIGH_DISAGREEMENT;
import static java.util.Collections.emptyList;

public class HighDisagreementEstimationInsightResolver implements EstimationInsightResolver {

    private final int minimumParticipants;
    private final int minimumSpreadSteps;

    public HighDisagreementEstimationInsightResolver(int minimumParticipants, int minimumSpreadSteps) {
        this.minimumParticipants = minimumParticipants;
        this.minimumSpreadSteps = minimumSpreadSteps;
    }

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        if (current.getEstimates().size() < minimumParticipants) {
            return emptyList();
        }

        var deckIndexByEstimate = current.deckIndexByEstimate();
        var votesByEstimate = current.votesByEstimate();

        var summary = deckIndexByEstimate
            .values().stream()
            .mapToInt(Integer::intValue)
            .summaryStatistics();

        var spread = summary.getMax() - summary.getMin();
        if (spread < minimumSpreadSteps) {
            return emptyList();
        }

        var minIndex = summary.getMin();
        var maxIndex = summary.getMax();

        var minVotes = deckIndexByEstimate.entrySet().stream()
            .filter(entry -> entry.getValue() == minIndex)
            .mapToLong(entry -> votesByEstimate.get(entry.getKey()))
            .sum();

        var maxVotes = deckIndexByEstimate.entrySet().stream()
            .filter(entry -> entry.getValue() == maxIndex)
            .mapToLong(entry -> votesByEstimate.get(entry.getKey()))
            .sum();

        if (minVotes == 1 || maxVotes == 1) {
            return emptyList();
        }

        return List.of(HIGH_DISAGREEMENT);
    }
}

