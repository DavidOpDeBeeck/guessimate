package app.dodb.guessimate.session.drivenadapter.sessionview;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.domain.Session;
import app.dodb.guessimate.session.domain.SessionState;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.anEstimationTO;
import static app.dodb.guessimate.session.domain.SessionTestConstants.SESSION_ID;
import static app.dodb.guessimate.session.domain.SessionTestConstants.anEstimation;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class SessionViewJpaRepositoryIntegrationTest {

    @Inject
    EntityManager entityManager;
    @Inject
    SessionViewJpaRepository repository;

    @Test
    void givenSessionExists_whenFindSession_thenReturnSession() {
        var sessionView = new SessionView(SESSION_ID_VALUE, new SessionViewData(Set.of(anEstimationTO())));
        entityManager.persist(sessionView);

        var actual = repository.findBy(SESSION_ID)
            .orElse(null);

        assertThat(actual)
            .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                .withIgnoreCollectionOrder(true)
                .withIgnoredFieldsOfTypes(LocalDateTime.class)
                .build())
            .isEqualTo(new Session(new SessionState(SESSION_ID, Set.of(anEstimation()))));
    }

    @Test
    void givenNoSessionExists_whenFindSession_thenReturnEmpty() {
        assertThat(repository.findBy(SESSION_ID)).isEmpty();
    }
}
