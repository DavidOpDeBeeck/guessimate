package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.FREQUENT_REVOTE;
import static java.util.Collections.emptyList;

public class FrequentRevoteEstimationInsightResolver implements EstimationInsightResolver {

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        if (previous.size() < 2) {
            return emptyList();
        }

        var currentVotes = current.votesByEstimate();
        var matchesPrevious = previous.stream()
            .sorted(Comparator.comparing(Estimation::getTimestamp).reversed())
            .limit(2)
            .allMatch(prev -> prev.votesByEstimate().equals(currentVotes));

        return matchesPrevious ? List.of(FREQUENT_REVOTE) : emptyList();
    }
}
