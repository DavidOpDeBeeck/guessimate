package app.dodb.guessimate.session.application;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.api.command.AddEstimationCommand;
import app.dodb.smd.api.command.bus.CommandBus;
import jakarta.inject.Inject;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.CONSENSUS;
import static app.dodb.guessimate.session.api.EstimationInsight.SOLO_VOTE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

@AcceptanceTest
public class SessionAcceptanceTest {

    @LocalServerPort
    int port;
    @Inject
    CommandBus commandBus;

    private RestClient client;

    @BeforeEach
    public void setup() {
        client = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @Test
    void whenCreateSession_thenReturnNewSessionInfo() {
        var createSessionResponse = client.post()
            .uri("/sessions")
            .retrieve()
            .body(SessionTO.class);

        assertThat(createSessionResponse).isNotNull()
            .extracting(SessionTO::sessionId, STRING)
            .matches("^[A-Za-z]+-[A-Za-z]+-[A-Za-z]+$");
    }

    @Test
    void givenSessionExists_whenFindSession_thenReturnSessionInfo() {
        var createSessionResponse = client.post()
            .uri("/sessions")
            .retrieve()
            .body(SessionTO.class);

        assertThat(createSessionResponse).isNotNull();

        var findSessionResponse = client.get()
            .uri("/sessions/{id}", createSessionResponse.sessionId())
            .retrieve()
            .body(SessionTO.class);

        assertThat(createSessionResponse).isEqualTo(findSessionResponse);
    }

    @Test
    void givenNoSessionExists_whenFindSession_thenReturn404() {
        assertThatThrownBy(() ->
            client.get()
                .uri("/sessions/{sessionId}", "non-existent-id")
                .retrieve()
                .toBodilessEntity())
            .isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    void givenSessionExists_whenFindEstimations_thenReturnEstimations() {
        var createSessionResponse = client.post()
            .uri("/sessions")
            .retrieve()
            .body(SessionTO.class);

        assertThat(createSessionResponse).isNotNull();

        var estimationId = commandBus.send(new AddEstimationCommand(createSessionResponse.sessionId(), aDeckTO(), List.of(ESTIMATE_VALUE)));

        var findEstimationsResponse = client.get()
            .uri("/sessions/{sessionId}/estimations", createSessionResponse.sessionId())
            .retrieve()
            .body(new ParameterizedTypeReference<Set<EstimationTO>>() {
            });

        assertThat(findEstimationsResponse)
            .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder()
                .withIgnoredFieldsOfTypes(LocalDateTime.class)
                .build())
            .containsExactly(new EstimationTO(estimationId, LocalDateTime.now(), aDeckTO(), List.of(ESTIMATE_VALUE), Set.of(SOLO_VOTE.name(), CONSENSUS.name()), 1, Map.of(ESTIMATE_VALUE, 1L)));
    }

    @Test
    void givenNoSessionExists_whenFindEstimations_thenReturn404() {
        assertThatThrownBy(() ->
            client.get()
                .uri("/sessions/{sessionId}/estimations", "non-existent-id")
                .retrieve()
                .toBodilessEntity())
            .isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    void givenSessionExists_whenFindEstimation_thenReturnEstimation() {
        var createSessionResponse = client.post()
            .uri("/sessions")
            .retrieve()
            .body(SessionTO.class);

        assertThat(createSessionResponse).isNotNull();

        var estimationId = commandBus.send(new AddEstimationCommand(createSessionResponse.sessionId(), aDeckTO(), List.of(ESTIMATE_VALUE)));

        var findEstimationResponse = client.get()
            .uri("/sessions/{sessionId}/estimations/{estimationId}", createSessionResponse.sessionId(), estimationId)
            .retrieve()
            .body(EstimationTO.class);

        assertThat(findEstimationResponse)
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                .withIgnoredFieldsOfTypes(LocalDateTime.class)
                .build())
            .isEqualTo(new EstimationTO(estimationId, LocalDateTime.now(), aDeckTO(), List.of(ESTIMATE_VALUE), Set.of(SOLO_VOTE.name(), CONSENSUS.name()), 1, Map.of(ESTIMATE_VALUE, 1L)));
    }

    @Test
    void givenNoSessionExists_whenFindEstimation_thenReturn404() {
        assertThatThrownBy(() ->
            client.get()
                .uri("/sessions/{sessionId}/estimations/{estimationId}", "non-existent-id", "non-existent-id")
                .retrieve()
                .toBodilessEntity())
            .isInstanceOf(HttpClientErrorException.NotFound.class);
    }
}
