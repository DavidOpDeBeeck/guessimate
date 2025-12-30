package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.domain.Lobby;
import app.dodb.guessimate.lobby.domain.SessionId;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.smd.api.event.EventHandler;
import app.dodb.smd.api.event.EventPublisher;
import app.dodb.smd.api.event.ProcessingGroup;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("lobby_creation")
public class LobbyCreationEventHandler {

    private final EventPublisher eventPublisher;
    private final QueryGateway queryGateway;

    public LobbyCreationEventHandler(EventPublisher eventPublisher, QueryGateway queryGateway) {
        this.eventPublisher = eventPublisher;
        this.queryGateway = queryGateway;
    }

    @EventHandler
    public void on(SessionCreatedEvent event) {
        var sessionId = new SessionId(event.sessionId());
        var defaultDeck = queryGateway.send(new FindDefaultDeckQuery());

        var lobby = new Lobby(sessionId, defaultDeck);
        lobby.consumeEvents().forEach(eventPublisher::publish);
    }
}
