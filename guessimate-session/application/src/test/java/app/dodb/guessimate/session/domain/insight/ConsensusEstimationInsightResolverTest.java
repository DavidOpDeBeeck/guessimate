package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.CONSENSUS;
import static app.dodb.guessimate.session.api.EstimationInsight.CONSENSUS_HOT_STREAK;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class ConsensusEstimationInsightResolverTest {

    @Test
    void resolve_whenConsensusWithNoPreviousEstimations_thenReturnsConsensus() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new ConsensusEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(CONSENSUS);
    }

    @Test
    void resolve_whenConsensusWithOnePreviousEstimation_thenReturnsConsensus() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("3");
        var previousEstimate2 = new Estimate("3");
        var previousEstimation = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var resolver = new ConsensusEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation));

        assertThat(insights).containsExactly(CONSENSUS);
    }

    @Test
    void resolve_whenConsensusWithTwoPreviousConsensusEstimations_thenReturnsConsensusAndHotStreak() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("3");
        var previousEstimate2 = new Estimate("3");
        var previousEstimation1 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(2), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var previousEstimate3 = new Estimate("8");
        var previousEstimate4 = new Estimate("8");
        var previousEstimation2 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate3, previousEstimate4), modifiedFibonacci());

        var resolver = new ConsensusEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation1, previousEstimation2));

        assertThat(insights).containsExactly(CONSENSUS, CONSENSUS_HOT_STREAK);
    }

    @Test
    void resolve_whenConsensusWithTwoPreviousButOneNotConsensus_thenReturnsConsensusOnly() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("3");
        var previousEstimate2 = new Estimate("3");
        var previousEstimation1 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(2), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var previousEstimate3 = new Estimate("8");
        var previousEstimate4 = new Estimate("13");
        var previousEstimation2 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate3, previousEstimate4), modifiedFibonacci());

        var resolver = new ConsensusEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation1, previousEstimation2));

        assertThat(insights).containsExactly(CONSENSUS);
    }

    @Test
    void resolve_whenConsensusWithMoreThanTwoPreviousConsensus_thenReturnsConsensusAndHotStreak() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("5");
        var thirdEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var previousEstimate1 = new Estimate("3");
        var previousEstimate2 = new Estimate("3");
        var previousEstimation1 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(3), List.of(previousEstimate1, previousEstimate2), modifiedFibonacci());

        var previousEstimate3 = new Estimate("8");
        var previousEstimate4 = new Estimate("8");
        var previousEstimation2 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(2), List.of(previousEstimate3, previousEstimate4), modifiedFibonacci());

        var previousEstimate5 = new Estimate("13");
        var previousEstimate6 = new Estimate("13");
        var previousEstimation3 = new Estimation(randomEstimationId(), LocalDateTime.now().minusMinutes(1), List.of(previousEstimate5, previousEstimate6), modifiedFibonacci());

        var resolver = new ConsensusEstimationInsightResolver();
        var insights = resolver.resolve(estimation, Set.of(previousEstimation1, previousEstimation2, previousEstimation3));

        assertThat(insights).containsExactly(CONSENSUS, CONSENSUS_HOT_STREAK);
    }

    @Test
    void resolve_whenNotConsensus_thenReturnsEmpty() {
        var firstEstimate = new Estimate("5");
        var secondEstimate = new Estimate("8");
        var thirdEstimate = new Estimate("13");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new ConsensusEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenNoEstimates_thenReturnsEmpty() {
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(), modifiedFibonacci());

        var resolver = new ConsensusEstimationInsightResolver();
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }
}
