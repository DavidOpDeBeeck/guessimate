package app.dodb.guessimate.lobby.drivingadapter.ws;

import app.dodb.guessimate.lobby.api.command.ConnectUserToLobbyCommand;
import app.dodb.guessimate.lobby.api.command.DisconnectUserFromLobbyCommand;
import app.dodb.guessimate.lobby.api.event.LobbyInfoSetEvent;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
import app.dodb.smd.api.command.CommandGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LobbyWebSocketHandlerTest {

    @Mock
    private CommandGateway commandGateway;
    @Mock
    private WebSocketContextManager contextManager;
    @Mock
    private WebSocketSession webSocketSession;

    @Test
    void bootstrapsLobbyInfoBeforeSubscribingSocketToBroadcasts() {
        WebSocketContext context = mock(WebSocketContext.class);
        LobbyInfoSetEvent snapshot = new LobbyInfoSetEvent("session", null, null, LobbyStatus.ESTIMATING, List.of(), null);

        when(contextManager.addSession(webSocketSession)).thenReturn(context);
        when(context.sessionId()).thenReturn("session");
        when(context.userId()).thenReturn("user");
        when(context.userName()).thenReturn(Optional.of("Alice"));
        when(commandGateway.send(any(ConnectUserToLobbyCommand.class))).thenReturn(snapshot);

        LobbyWebSocketHandler handler = new LobbyWebSocketHandler(commandGateway, contextManager);

        handler.afterConnectionEstablished(webSocketSession);

        InOrder inOrder = inOrder(contextManager, commandGateway, context);
        inOrder.verify(contextManager).addSession(webSocketSession);
        inOrder.verify(commandGateway).send(new ConnectUserToLobbyCommand("session", "user", Optional.of("Alice")));
        inOrder.verify(context).send(snapshot);
        inOrder.verify(contextManager).subscribe("session", "user");
    }

    @Test
    void unsubscribesSocketBeforeDispatchingDisconnectCommand() {
        WebSocketContext context = mock(WebSocketContext.class);
        when(contextManager.getContextBy(webSocketSession)).thenReturn(context);
        when(context.sessionId()).thenReturn("session");
        when(context.userId()).thenReturn("user");

        LobbyWebSocketHandler handler = new LobbyWebSocketHandler(commandGateway, contextManager);

        handler.afterConnectionClosed(webSocketSession, CloseStatus.NORMAL);

        InOrder inOrder = inOrder(contextManager, commandGateway);
        inOrder.verify(contextManager).getContextBy(webSocketSession);
        inOrder.verify(contextManager).unsubscribe("session", "user");
        inOrder.verify(contextManager).removeSession(webSocketSession);
        inOrder.verify(commandGateway).send(new DisconnectUserFromLobbyCommand("session", "user"));
    }
}
