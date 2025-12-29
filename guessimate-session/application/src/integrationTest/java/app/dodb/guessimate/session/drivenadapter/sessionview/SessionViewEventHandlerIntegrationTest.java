package app.dodb.guessimate.session.drivenadapter.sessionview;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.api.event.EstimationRemovedEvent;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.smd.api.event.bus.EventBus;
import jakarta.inject.Inject;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.SOLO_VOTE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class SessionViewEventHandlerIntegrationTest {

    @Inject
    EventBus eventBus;
    @Inject
    SessionViewSpringRepository repository;

    @Test
    void handleSessionCreatedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE)
            .orElse(null);

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(new SessionView(SESSION_ID_VALUE, new SessionViewData(emptySet())));
    }

    @Test
    void handleEstimationAddedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new EstimationAddedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, LocalDateTime.now(), aDeckTO(), List.of(ESTIMATE_VALUE), Set.of(SOLO_VOTE)));

        var actual = repository.findById(SESSION_ID_VALUE)
            .orElse(null);

        assertThat(actual)
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                .withIgnoredFieldsOfTypes(LocalDateTime.class)
                .build())
            .isEqualTo(new SessionView(SESSION_ID_VALUE, new SessionViewData(Set.of(new EstimationTO(ESTIMATION_ID_VALUE, LocalDateTime.now(), aDeckTO(), List.of(ESTIMATE_VALUE),
                Set.of(SOLO_VOTE.name()), 1, Map.of(ESTIMATE_VALUE, 1L))))));
    }

    @Test
    void handleEstimationRemovedEvent() {
        eventBus.publish(new SessionCreatedEvent(SESSION_ID_VALUE));
        eventBus.publish(new EstimationAddedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE, LocalDateTime.now(), aDeckTO(), List.of(ESTIMATE_VALUE), Set.of(SOLO_VOTE)));
        eventBus.publish(new EstimationRemovedEvent(SESSION_ID_VALUE, ESTIMATION_ID_VALUE));

        var actual = repository.findById(SESSION_ID_VALUE)
            .orElse(null);

        assertThat(actual)
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                .withIgnoredFieldsOfTypes(LocalDateTime.class)
                .build())
            .isEqualTo(new SessionView(SESSION_ID_VALUE, new SessionViewData(emptySet())));
    }
}