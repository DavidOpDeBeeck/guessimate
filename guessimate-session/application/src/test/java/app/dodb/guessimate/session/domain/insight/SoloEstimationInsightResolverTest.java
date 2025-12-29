package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static app.dodb.guessimate.session.api.EstimationInsight.SOLO_VOTE;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class SoloEstimationInsightResolverTest {

    @Test
    void resolve_whenOnlyOneEstimate_thenReturnsSoloVote() {
        var firstEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate), modifiedFibonacci());

        var resolver = new SoloEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(SOLO_VOTE);
    }

    @Test
    void resolve_whenTwoEstimates_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate), modifiedFibonacci());

        var resolver = new SoloEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenMultipleEstimates_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var thirdEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new SoloEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenNoEstimates_thenReturnsEmpty() {
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(), modifiedFibonacci());

        var resolver = new SoloEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }
}
