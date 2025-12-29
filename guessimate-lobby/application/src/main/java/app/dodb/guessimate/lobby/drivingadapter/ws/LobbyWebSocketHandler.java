package app.dodb.guessimate.lobby.drivingadapter.ws;

import app.dodb.guessimate.lobby.api.command.ConnectUserToLobbyCommand;
import app.dodb.guessimate.lobby.api.command.DisconnectUserFromLobbyCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.smd.api.command.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class LobbyWebSocketHandler extends TextWebSocketHandler {

    private final CommandGateway commandGateway;
    private final WebSocketContextManager contextManager;

    public LobbyWebSocketHandler(CommandGateway commandGateway,
                                 WebSocketContextManager contextManager) {
        this.commandGateway = commandGateway;
        this.contextManager = contextManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        var context = contextManager.addSession(session);
        commandGateway.send(new ConnectUserToLobbyCommand(context.sessionId(), context.userId(), context.userName()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        var context = contextManager.getContextBy(session);
        commandGateway.send(new UserActionCommand<>(context.sessionId(), context.userId(), context.receive(message)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        var context = contextManager.removeSession(session);
        commandGateway.send(new DisconnectUserFromLobbyCommand(context.sessionId(), context.userId()));
    }
}