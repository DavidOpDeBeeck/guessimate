package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.ALL_UNIQUE;
import static java.util.Collections.emptyList;

public class AllUniqueEstimationInsightResolver implements EstimationInsightResolver {

    private final int minimumParticipants;

    public AllUniqueEstimationInsightResolver(int minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        var uniqueEstimates = current.votesByEstimate().size();
        var totalVotes = current.getEstimates().size();

        return totalVotes >= minimumParticipants && uniqueEstimates == totalVotes
            ? List.of(ALL_UNIQUE)
            : emptyList();
    }
}
