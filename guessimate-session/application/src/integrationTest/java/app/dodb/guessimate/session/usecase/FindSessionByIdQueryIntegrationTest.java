package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.IntegrationTest;
import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.api.query.FindSessionByIdQuery;
import app.dodb.guessimate.session.drivenadapter.sessionview.SessionView;
import app.dodb.guessimate.session.drivenadapter.sessionview.SessionViewData;
import app.dodb.smd.api.query.bus.QueryBus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.anEstimationTO;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class FindSessionByIdQueryIntegrationTest {

    @Inject
    QueryBus queryBus;
    @Inject
    EntityManager entityManager;

    @Test
    void givenSessionExists_whenFindSessionById_thenReturnSession() {
        var sessionView = new SessionView(SESSION_ID_VALUE, new SessionViewData(Set.of(anEstimationTO())));
        entityManager.persist(sessionView);

        var actual = queryBus.send(new FindSessionByIdQuery(SESSION_ID_VALUE))
            .orElse(null);

        assertThat(actual)
            .isEqualTo(new SessionTO(SESSION_ID_VALUE));
    }

    @Test
    void givenNoSessionExists_whenFindSessionById_thenReturnEmpty() {
        assertThat(queryBus.send(new FindSessionByIdQuery(SESSION_ID_VALUE))).isEmpty();
    }
}
