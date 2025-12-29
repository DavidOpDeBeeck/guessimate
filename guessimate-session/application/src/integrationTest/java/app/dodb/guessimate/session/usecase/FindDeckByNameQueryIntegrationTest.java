package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.api.query.FindDeckByNameQuery;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindDeckByNameQueryIntegrationTest {

    @Inject
    QueryBus queryBus;

    @Test
    void findByName_whenDeckExists_returnsDeck() {
        var actual = queryBus.send(new FindDeckByNameQuery("T-shirt"));

        assertThat(actual).isPresent();
        assertThat(actual.get().deckName()).isEqualTo("T-shirt");
        assertThat(actual.get().cards()).containsExactly("XS", "S", "M", "L", "XL", "XXL");
    }

    @Test
    void findByName_whenDeckDoesNotExist_returnsEmpty() {
        var actual = queryBus.send(new FindDeckByNameQuery("Non-existent"));

        assertThat(actual).isEmpty();
    }
}
