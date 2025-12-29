package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static app.dodb.guessimate.session.api.EstimationInsight.NEAR_CONSENSUS;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class NearConsensusEstimationInsightResolverTest {

    @Test
    void resolve_whenMajorityMeetsThreshold_thenReturnsNearConsensus() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new NearConsensusEstimationInsightResolver(0.75);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(NEAR_CONSENSUS);
    }

    @Test
    void resolve_whenMajorityExactlyMeetsThreshold_thenReturnsNearConsensus() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("8");
        var fifthEstimate = new Estimate("8");
        var sixthEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate, fifthEstimate, sixthEstimate), modifiedFibonacci());

        var resolver = new NearConsensusEstimationInsightResolver(0.5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(NEAR_CONSENSUS);
    }

    @Test
    void resolve_whenMajorityBelowThreshold_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("8");
        var fourthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new NearConsensusEstimationInsightResolver(0.75);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenConsensus_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new NearConsensusEstimationInsightResolver(0.75);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenNoEstimates_thenReturnsEmpty() {
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(), modifiedFibonacci());

        var resolver = new NearConsensusEstimationInsightResolver(0.75);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenSingleEstimate_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate), modifiedFibonacci());

        var resolver = new NearConsensusEstimationInsightResolver(0.75);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenAllUniqueEstimates_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("2");
        var thirdEstimate = new Estimate("3");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new NearConsensusEstimationInsightResolver(0.75);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }
}
