package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static app.dodb.guessimate.session.api.EstimationInsight.ALL_UNIQUE;
import static app.dodb.guessimate.session.domain.SessionTestConstants.modifiedFibonacci;
import static app.dodb.guessimate.session.domain.SessionTestConstants.randomEstimationId;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class AllUniqueEstimationInsightResolverTest {

    @Test
    void resolve_whenAllEstimatesAreUniqueAndMeetsMinimumParticipants_thenReturnsAllUnique() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("2");
        var thirdEstimate = new Estimate("3");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new AllUniqueEstimationInsightResolver(4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(ALL_UNIQUE);
    }

    @Test
    void resolve_whenNotEnoughParticipants_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("2");
        var thirdEstimate = new Estimate("3");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate), modifiedFibonacci());

        var resolver = new AllUniqueEstimationInsightResolver(4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenEstimatesAreNotAllUnique_thenReturnsEmpty() {
        var firstEstimate = new Estimate("2");
        var secondEstimate = new Estimate("2");
        var thirdEstimate = new Estimate("3");
        var fourthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate), modifiedFibonacci());

        var resolver = new AllUniqueEstimationInsightResolver(4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenNoEstimates_thenReturnsEmpty() {
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(), modifiedFibonacci());

        var resolver = new AllUniqueEstimationInsightResolver(4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenOnlyOneEstimate_thenReturnsEmpty() {
        var firstEstimate = new Estimate("1");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate), modifiedFibonacci());

        var resolver = new AllUniqueEstimationInsightResolver(4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }

    @Test
    void resolve_whenExceedsMinimumParticipants_thenReturnsAllUnique() {
        var firstEstimate = new Estimate("1");
        var secondEstimate = new Estimate("2");
        var thirdEstimate = new Estimate("3");
        var fourthEstimate = new Estimate("5");
        var fifthEstimate = new Estimate("8");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate, fifthEstimate), modifiedFibonacci());

        var resolver = new AllUniqueEstimationInsightResolver(4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).containsExactly(ALL_UNIQUE);
    }

    @Test
    void resolve_whenEnoughParticipantsButNotAllUnique_thenReturnsEmpty() {
        var firstEstimate = new Estimate("2");
        var secondEstimate = new Estimate("2");
        var thirdEstimate = new Estimate("2");
        var fourthEstimate = new Estimate("5");
        var fifthEstimate = new Estimate("5");
        var estimation = new Estimation(randomEstimationId(), LocalDateTime.now(), List.of(firstEstimate, secondEstimate, thirdEstimate, fourthEstimate, fifthEstimate), modifiedFibonacci());

        var resolver = new AllUniqueEstimationInsightResolver(4);
        var insights = resolver.resolve(estimation, emptySet());

        assertThat(insights).isEmpty();
    }
}