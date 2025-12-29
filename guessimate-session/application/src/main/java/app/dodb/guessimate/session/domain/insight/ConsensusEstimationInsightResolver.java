package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.CONSENSUS;
import static app.dodb.guessimate.session.api.EstimationInsight.CONSENSUS_HOT_STREAK;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;

public class ConsensusEstimationInsightResolver implements EstimationInsightResolver {

    private static final int HOT_STREAK_COUNT = 2;

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        return current.isConsensus() ? resolveConsensus(previous) : emptyList();
    }

    private static List<EstimationInsight> resolveConsensus(Set<Estimation> previous) {
        if (previous.size() < HOT_STREAK_COUNT) {
            return List.of(CONSENSUS);
        }

        boolean hotStreak = previous.stream()
            .sorted(comparing(Estimation::getTimestamp).reversed())
            .limit(HOT_STREAK_COUNT)
            .allMatch(Estimation::isConsensus);

        return hotStreak ? List.of(CONSENSUS, CONSENSUS_HOT_STREAK) : List.of(CONSENSUS);
    }
}
