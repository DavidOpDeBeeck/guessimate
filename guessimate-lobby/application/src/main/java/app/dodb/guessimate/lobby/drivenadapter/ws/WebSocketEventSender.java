package app.dodb.guessimate.lobby.drivenadapter.ws;

import app.dodb.guessimate.lobby.api.event.LobbyInfoSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.WebSocketEvent;
import app.dodb.guessimate.lobby.api.query.FindLobbyInfoForUserQuery;
import app.dodb.guessimate.lobby.drivingadapter.ws.WebSocketContextManager;
import app.dodb.smd.api.event.EventHandler;
import app.dodb.smd.api.event.ProcessingGroup;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ProcessingGroup("web_socket_sender")
public class WebSocketEventSender {

    private final QueryGateway queryGateway;
    private final WebSocketContextManager contextManager;

    public WebSocketEventSender(QueryGateway queryGateway,
                                WebSocketContextManager contextManager) {
        this.queryGateway = queryGateway;
        this.contextManager = contextManager;
    }

    @EventHandler(order = 1)
    public void link(UserConnectedEvent event) {
        contextManager.link(event.sessionId(), event.userId());
    }

    @EventHandler(order = 1)
    public void unlink(UserDisconnectedEvent event) {
        contextManager.unlink(event.sessionId(), event.userId());
    }

    @EventHandler(order = 2)
    public void on(WebSocketEvent event) {
        var contexts = contextManager.getContextsBySessionId(event.sessionId());

        for (var context : contexts) {
            if (event instanceof UserConnectedEvent userConnectedEvent) {
                if (context.userId().equals(userConnectedEvent.userId())) {
                    findLobbyInfo(userConnectedEvent.sessionId(), userConnectedEvent.userId())
                        .ifPresent(context::send);
                    continue;
                }
            }
            context.send(event);
        }
    }

    private Optional<LobbyInfoSetEvent> findLobbyInfo(String sessionId, String userId) {
        return queryGateway.send(new FindLobbyInfoForUserQuery(sessionId, userId))
            .map(LobbyInfoSetEvent::from);
    }
}
