package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static app.dodb.guessimate.session.api.EstimationInsight.HIGH_DISAGREEMENT;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class HighDisagreementEstimationInsightResolverTest {

    @Test
    void resolve_whenSpreadMeetsMinimumAndMultipleVotesOnBothEnds_thenReturnsHighDisagreement() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("13");
        var fourthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(HIGH_DISAGREEMENT);
    }

    @Test
    void resolve_whenNotEnoughParticipants_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenSpreadBelowMinimum_thenReturnsEmpty() {
        var firstEstimate = new Estimate("3");
        var secondEstimate = new Estimate("3");
        var thirdEstimate = new Estimate("5");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenMinVoteIsSingleOutlier_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("13");
        var thirdEstimate = new Estimate("13");
        var fourthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenMaxVoteIsSingleOutlier_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("1");
        var fourthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenSpreadExactlyMeetsMinimum_thenReturnsHighDisagreement() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("8");
        var fourthEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(HIGH_DISAGREEMENT);
    }

    @Test
    void resolve_whenNoEstimates_thenReturnsEmpty() {
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenMultipleEstimatesAtBothEndsAndHighSpread_thenReturnsHighDisagreement() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("1");
        var thirdEstimate = new Estimate("1");
        var fourthEstimate = new Estimate("13");
        var fifthEstimate = new Estimate("13");
        var sixthEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate, fifthEstimate, sixthEstimate), modifiedFibonacci());

        var resolver = new HighDisagreementEstimationInsightResolver(4, 5);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(HIGH_DISAGREEMENT);
    }
}
