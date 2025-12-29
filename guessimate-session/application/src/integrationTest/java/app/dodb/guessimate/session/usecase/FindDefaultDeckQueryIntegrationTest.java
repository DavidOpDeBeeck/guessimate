package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindDefaultDeckQueryIntegrationTest {

    @Inject
    QueryBus queryBus;

    @Test
    void findDefaultDeck_returnsModifiedFibonacci() {
        var actual = queryBus.send(new FindDefaultDeckQuery());

        assertThat(actual.deckName()).isEqualTo("Modified Fibonacci");
        assertThat(actual.cards()).containsExactly("0", "0.5", "1", "2", "3", "5", "8", "13");
    }
}
