package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.NEAR_CONSENSUS;
import static java.util.Collections.emptyList;

public class NearConsensusEstimationInsightResolver implements EstimationInsightResolver {

    private final double threshold;

    public NearConsensusEstimationInsightResolver(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        if (current.isConsensus()) {
            return emptyList();
        }

        var votesByEstimate = current.votesByEstimate();
        var totalVotes = current.getEstimates().size();

        var maxVotes = votesByEstimate.values().stream()
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);

        var percentage = maxVotes / (double) totalVotes;
        return percentage >= threshold ? List.of(NEAR_CONSENSUS) : emptyList();
    }
}
