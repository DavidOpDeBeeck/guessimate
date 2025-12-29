package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.OUTLIER_PRESENT;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingLong;

public class OutlierPresentEstimationInsightResolver implements EstimationInsightResolver {

    private final int minimumParticipants;
    private final int minimumOutlierDistance;

    public OutlierPresentEstimationInsightResolver(int minimumParticipants, int minimumOutlierDistance) {
        this.minimumParticipants = minimumParticipants;
        this.minimumOutlierDistance = minimumOutlierDistance;
    }

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        if (current.getEstimates().size() < minimumParticipants) {
            return emptyList();
        }

        var votesByEstimate = current.votesByEstimate();
        var deckIndexByEstimate = current.deckIndexByEstimate();

        var mostVotedEstimate = votesByEstimate.entrySet().stream()
            .max(comparingLong(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElseThrow();
        var leastVotedEstimate = votesByEstimate.entrySet().stream()
            .min(comparingLong(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElseThrow();

        if (votesByEstimate.get(leastVotedEstimate) != 1L) {
            return emptyList();
        }

        var mostVotedDeckIndex = deckIndexByEstimate.get(mostVotedEstimate);
        var leastVotedDeckIndex = deckIndexByEstimate.get(leastVotedEstimate);

        var distance = Math.abs(mostVotedDeckIndex - leastVotedDeckIndex);
        return distance >= minimumOutlierDistance ? List.of(OUTLIER_PRESENT) : emptyList();
    }

}

