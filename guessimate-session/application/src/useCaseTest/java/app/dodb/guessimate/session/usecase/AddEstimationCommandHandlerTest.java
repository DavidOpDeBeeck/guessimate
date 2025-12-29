package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.api.command.AddEstimationCommand;
import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.domain.Session;
import app.dodb.guessimate.session.domain.SessionState;
import app.dodb.guessimate.session.usecase.stubs.SessionRepositoryStub;
import app.dodb.smd.test.SMDTestExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.ANOTHER_ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static app.dodb.guessimate.session.domain.SessionTestConstants.SESSION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UseCaseTest
class AddEstimationCommandHandlerTest {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    @Inject
    private SMDTestExtension smd;
    @Inject
    private SessionRepositoryStub sessionRepository;

    @Test
    void givenNoSessionExists_whenAddEstimation_thenThrowException() {
        assertThatThrownBy(() -> smd.send(new AddEstimationCommand(SESSION_ID_VALUE, aDeckTO(), List.of(ESTIMATE_VALUE))))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("No session found with id: session-id");
    }

    @Test
    void givenSessionExists_whenAddEstimation_thenCreateEstimation() {
        sessionRepository.stubSession(SESSION_ID, new Session(new SessionState(SESSION_ID, new HashSet<>())));
        smd.stubTimestamp(TIMESTAMP);

        String estimationId = smd.send(new AddEstimationCommand(SESSION_ID_VALUE, aDeckTO(), List.of(ESTIMATE_VALUE, ANOTHER_ESTIMATE_VALUE)));

        assertThat(smd.getEvents())
            .containsExactly(new EstimationAddedEvent(SESSION_ID_VALUE, estimationId, TIMESTAMP, aDeckTO(), List.of(ESTIMATE_VALUE, ANOTHER_ESTIMATE_VALUE), Set.of()));
    }
}