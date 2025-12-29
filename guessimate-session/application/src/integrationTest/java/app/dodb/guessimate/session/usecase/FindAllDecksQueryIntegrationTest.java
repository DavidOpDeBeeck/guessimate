package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.api.query.FindAllDecksQuery;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindAllDecksQueryIntegrationTest {

    @Inject
    QueryBus queryBus;

    @Test
    void findAll_returnsAllDecksInOrder() {
        var actual = queryBus.send(new FindAllDecksQuery());

        assertThat(actual)
            .extracting(DeckTO::deckName)
            .containsExactly(
                "Modified Fibonacci",
                "Fibonacci",
                "Powers of 2",
                "T-shirt",
                "Sequential",
                "Risk Level"
            );
    }

    @Test
    void findAll_containsExpectedDecks() {
        var actual = queryBus.send(new FindAllDecksQuery());

        assertThat(actual).contains(
            new DeckTO("Modified Fibonacci", List.of("0", "0.5", "1", "2", "3", "5", "8", "13")),
            new DeckTO("Fibonacci", List.of("0", "1", "2", "3", "5", "8", "13", "21")),
            new DeckTO("Powers of 2", List.of("0", "1", "2", "4", "8", "16", "32", "64")),
            new DeckTO("T-shirt", List.of("XS", "S", "M", "L", "XL", "XXL")),
            new DeckTO("Sequential", List.of("1", "2", "3", "4", "5", "6", "7", "8")),
            new DeckTO("Risk Level", List.of("L", "M", "H"))
        );
    }
}
