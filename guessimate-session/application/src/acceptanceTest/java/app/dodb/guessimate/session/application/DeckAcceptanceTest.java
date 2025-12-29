package app.dodb.guessimate.session.application;

import app.dodb.guessimate.session.api.deck.DeckTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
public class DeckAcceptanceTest {

    @LocalServerPort
    int port;

    private RestClient client;

    @BeforeEach
    public void setup() {
        client = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @Test
    void findAllDecks() {
        var findAllDecksResponse = client.get()
            .uri("/decks")
            .retrieve()
            .body(new ParameterizedTypeReference<Set<DeckTO>>() {
            });

        assertThat(findAllDecksResponse).isNotNull()
            .containsExactlyInAnyOrder(
                new DeckTO("Modified Fibonacci", List.of("0", "0.5", "1", "2", "3", "5", "8", "13")),
                new DeckTO("Fibonacci", List.of("0", "1", "2", "3", "5", "8", "13", "21")),
                new DeckTO("Powers of 2", List.of("0", "1", "2", "4", "8", "16", "32", "64")),
                new DeckTO("T-shirt", List.of("XS", "S", "M", "L", "XL", "XXL")),
                new DeckTO("Sequential", List.of("1", "2", "3", "4", "5", "6", "7", "8")),
                new DeckTO("Risk Level", List.of("L", "M", "H"))
            );
    }
}
