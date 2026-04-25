package app.dodb.guessimate.lobby.drivenadapter.ws;

import app.dodb.guessimate.lobby.api.event.WebSocketEvent;
import app.dodb.guessimate.lobby.drivingadapter.ws.WebSocketContextManager;
import app.dodb.smd.api.event.EventHandler;
import app.dodb.smd.api.event.ProcessingGroup;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("web_socket_sender")
public class WebSocketEventSender {

    private final WebSocketContextManager contextManager;

    public WebSocketEventSender(WebSocketContextManager contextManager) {
        this.contextManager = contextManager;
    }

    @EventHandler
    public void on(WebSocketEvent event) {
        var contexts = contextManager.getContextsBySessionId(event.sessionId());

        for (var context : contexts) {
            context.send(event);
        }
    }
}
