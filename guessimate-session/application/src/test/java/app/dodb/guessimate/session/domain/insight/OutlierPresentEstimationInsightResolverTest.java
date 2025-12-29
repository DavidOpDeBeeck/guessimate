package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static app.dodb.guessimate.session.api.EstimationInsight.OUTLIER_PRESENT;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class OutlierPresentEstimationInsightResolverTest {

    @Test
    void resolve_whenSingleOutlierWithSufficientDistance_thenReturnsOutlierPresent() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 3);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(OUTLIER_PRESENT);
    }

    @Test
    void resolve_whenNotEnoughParticipants_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 3);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenDistanceBelowMinimum_thenReturnsEmpty() {
        var firstEstimate = new Estimate("3");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenLeastVotedHasMultipleVotes_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 3);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenConsensus_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 3);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenDistanceExactlyMeetsMinimum_thenReturnsOutlierPresent() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("8");
        var thirdEstimate = new Estimate("8");
        var fourthEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(OUTLIER_PRESENT);
    }

    @Test
    void resolve_whenNoEstimates_thenReturnsEmpty() {
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 3);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenOutlierIsHigherThanMajority_thenReturnsOutlierPresent() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("1");
        var fourthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(OUTLIER_PRESENT);
    }

    @Test
    void resolve_whenMultipleOutliers_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new OutlierPresentEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }
}
