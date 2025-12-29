package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.SOLO_VOTE;
import static java.util.Collections.emptyList;

public class SoloEstimationInsightResolver implements EstimationInsightResolver {

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        if (current.getEstimates().size() == 1) {
            return List.of(SOLO_VOTE);
        }
        return emptyList();
    }
}
