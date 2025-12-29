package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.api.query.FindEstimationByIdQuery;
import app.dodb.guessimate.session.drivenadapter.sessionview.SessionView;
import app.dodb.guessimate.session.drivenadapter.sessionview.SessionViewData;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.anEstimationTO;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindEstimationByIdQueryIntegrationTest {

    @Inject
    QueryBus queryBus;
    @Inject
    EntityManager entityManager;

    @Test
    void givenSessionExists_whenFindEstimationById_thenReturnEstimations() {
        var sessionView = new SessionView(SESSION_ID_VALUE, new SessionViewData(Set.of(anEstimationTO())));
        entityManager.persist(sessionView);

        var actual = queryBus.send(new FindEstimationByIdQuery(SESSION_ID_VALUE, ESTIMATION_ID_VALUE))
            .orElse(null);

        assertThat(actual)
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                .withIgnoredFieldsOfTypes(LocalDateTime.class)
                .build())
            .isEqualTo(anEstimationTO());
    }

    @Test
    void givenNoSessionExists_whenFindEstimationById_thenReturnEmpty() {
        assertThat(queryBus.send(new FindEstimationByIdQuery(SESSION_ID_VALUE, ESTIMATION_ID_VALUE))).isEmpty();
    }
}
