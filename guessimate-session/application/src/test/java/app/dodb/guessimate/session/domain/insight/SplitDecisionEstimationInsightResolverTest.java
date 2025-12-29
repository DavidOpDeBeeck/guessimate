package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static app.dodb.guessimate.session.api.EstimationInsight.SPLIT_DECISION;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class SplitDecisionEstimationInsightResolverTest {

    @Test
    void resolve_whenTopTwoAreCloseAndDominate_thenReturnsSplitDecision() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("8");
        var fourthEstimate = new Estimate("8");
        var fifthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate, fifthEstimate), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 1, 0.6);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(SPLIT_DECISION);
    }

    @Test
    void resolve_whenTopTwoAreExactlyTied_thenReturnsSplitDecision() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("8");
        var fourthEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 0, 1.0);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(SPLIT_DECISION);
    }

    @Test
    void resolve_whenNotEnoughParticipants_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 1, 0.6);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenOnlyOneEstimateValue_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 1, 0.6);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenVoteDifferenceExceedsMaximum_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("8");
        var fifthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate, fifthEstimate), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 1, 0.6);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenTopTwoDoNotDominate_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var thirdEstimate = new Estimate("13");
        var fourthEstimate = new Estimate("21");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 0, 0.6);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenNoEstimates_thenReturnsEmpty() {
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 1, 0.6);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenTopTwoExactlyMeetThreshold_thenReturnsSplitDecision() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("8");
        var fourthEstimate = new Estimate("8");
        var fifthEstimate = new Estimate("13");
        var sixthEstimate = new Estimate("21");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate, fifthEstimate, sixthEstimate), modifiedFibonacci());

        var resolver = new SplitDecisionEstimationInsightResolver(4, 0, 0.66);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(SPLIT_DECISION);
    }
}
