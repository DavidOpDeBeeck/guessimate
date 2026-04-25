package app.dodb.guessimate.application;

import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.drivenadapter.sessionview.SessionViewSpringRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
    classes = GuessimateApplication.class,
    webEnvironment = RANDOM_PORT
)
class CreateSessionAcceptanceTest {

    @LocalServerPort
    int port;
    @Inject
    SessionViewSpringRepository sessionViewSpringRepository;
    @Inject
    ApplicationContext context;

    private RestClient client;

    @BeforeEach
    void setUp() {
        client = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @Test
    void createSessionPersistsAndReturnsSessionWhenOpenInViewIsDisabled() {
        var createdSession = client.post()
            .uri("/sessions")
            .retrieve()
            .body(SessionTO.class);

        assertThat(createdSession).isNotNull()
            .extracting(SessionTO::sessionId, STRING)
            .matches("^[A-Za-z]+-[A-Za-z]+-[A-Za-z]+$");

        assertThat(sessionViewSpringRepository.findById(createdSession.sessionId()))
            .describedAs("session projection should be visible immediately after POST /sessions returns")
            .isPresent();

        var fetchedSession = client.get()
            .uri("/sessions/{sessionId}", createdSession.sessionId())
            .retrieve()
            .body(SessionTO.class);

        assertThat(fetchedSession).isEqualTo(createdSession);
    }
}
