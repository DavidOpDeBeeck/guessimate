package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.FREQUENT_REVOTE;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class FrequentRevoteEstimationInsightResolverTest {

    @Test
    void resolve_whenCurrentMatchesLastTwoPreviousEstimations_thenReturnsFrequentRevote() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("5");
        var previousEstimate2 = new Estimate("8");
        var previousEstimation1 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(2), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var previousEstimate3 = new Estimate("5");
        var previousEstimate4 = new Estimate("8");
        var previousEstimation2 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate3, previousEstimate4), modifiedFibonacci());

        var resolver = new FrequentRevoteEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation1, previousEstimation2));

        assertThat(insights).containsExactly(FREQUENT_REVOTE);
    }

    @Test
    void resolve_whenCurrentMatchesOnlyOnePreviousEstimation_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("5");
        var previousEstimate2 = new Estimate("8");
        var previousEstimation1 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(2), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var previousEstimate3 = new Estimate("3");
        var previousEstimate4 = new Estimate("13");
        var previousEstimation2 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate3, previousEstimate4), modifiedFibonacci());

        var resolver = new FrequentRevoteEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation1, previousEstimation2));

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenCurrentMatchesMoreThanTwoPreviousEstimations_thenReturnsFrequentRevote() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("5");
        var previousEstimate2 = new Estimate("8");
        var previousEstimation1 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(3), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var previousEstimate3 = new Estimate("5");
        var previousEstimate4 = new Estimate("8");
        var previousEstimation2 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(2), List.of(previousEstimate3, previousEstimate4), modifiedFibonacci());

        var previousEstimate5 = new Estimate("5");
        var previousEstimate6 = new Estimate("8");
        var previousEstimation3 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate5, previousEstimate6), modifiedFibonacci());

        var resolver = new FrequentRevoteEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation1, previousEstimation2, previousEstimation3));

        assertThat(insights).containsExactly(FREQUENT_REVOTE);
    }

    @Test
    void resolve_whenNoPreviousEstimations_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate), modifiedFibonacci());

        var resolver = new FrequentRevoteEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenOnlyOnePreviousEstimation_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("5");
        var previousEstimate2 = new Estimate("8");
        var previousEstimation = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var resolver = new FrequentRevoteEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation));

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenCurrentDifferentFromPrevious_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("3");
        var previousEstimate2 = new Estimate("13");
        var previousEstimation1 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(2), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var previousEstimate3 = new Estimate("1");
        var previousEstimate4 = new Estimate("2");
        var previousEstimation2 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate3, previousEstimate4), modifiedFibonacci());

        var resolver = new FrequentRevoteEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation1, previousEstimation2));

        assertThat(insights).isEmpty();
    }
}
