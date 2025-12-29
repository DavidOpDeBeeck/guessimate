package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Set;

public interface EstimationInsightResolver {

    List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous);
}
