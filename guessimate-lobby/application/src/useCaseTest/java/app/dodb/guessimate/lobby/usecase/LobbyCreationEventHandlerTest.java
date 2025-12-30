package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.event.LobbyCreatedEvent;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.smd.test.SMDTestExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.aDeckTO;
import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class LobbyCreationEventHandlerTest {

    @Inject
    protected SMDTestExtension smd;

    @BeforeEach
    void setUp() {
        smd.stubQuery(new FindDefaultDeckQuery(), aDeckTO());
    }

    @Test
    void createsLobbyWhenSessionIsCreated() {
        smd.send(new SessionCreatedEvent(SESSION_ID_VALUE));

        assertThat(smd.getEvents())
            .containsExactly(new LobbyCreatedEvent(SESSION_ID_VALUE));
    }
}
