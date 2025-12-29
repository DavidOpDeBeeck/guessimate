package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.api.query.FindEstimationsBySessionIdQuery;
import app.dodb.guessimate.session.drivenadapter.sessionview.SessionView;
import app.dodb.guessimate.session.drivenadapter.sessionview.SessionViewData;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.anEstimationTO;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindEstimationsBySessionIdQueryIntegrationTest {

    @Inject
    QueryBus queryBus;
    @Inject
    EntityManager entityManager;

    @Test
    void givenSessionExists_whenFindEstimationsBySessionId_thenReturnEstimations() {
        var sessionView = new SessionView(SESSION_ID_VALUE, new SessionViewData(Set.of(anEstimationTO())));
        entityManager.persist(sessionView);

        var actual = queryBus.send(new FindEstimationsBySessionIdQuery(SESSION_ID_VALUE))
            .orElse(null);

        assertThat(actual)
            .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder()
                .withIgnoredFieldsOfTypes(LocalDateTime.class)
                .build())
            .containsExactly(anEstimationTO());
    }

    @Test
    void givenNoSessionExists_whenFindEstimationsBySessionId_thenReturnEmpty() {
        assertThat(queryBus.send(new FindEstimationsBySessionIdQuery(SESSION_ID_VALUE))).isEmpty();
    }
}
