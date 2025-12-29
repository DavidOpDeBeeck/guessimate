package app.dodb.guessimate.lobby.drivenadapter.ws;

import app.dodb.guessimate.lobby.domain.UserId;
import app.dodb.guessimate.lobby.drivingadapter.ws.WebSocketContextManager;
import app.dodb.guessimate.lobby.port.UserConnectivityChecker;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUserConnectivityChecker implements UserConnectivityChecker {

    private final WebSocketContextManager contextManager;

    public WebSocketUserConnectivityChecker(WebSocketContextManager contextManager) {
        this.contextManager = contextManager;
    }

    @Override
    public boolean isConnected(UserId userId) {
        return contextManager.getContextByUserId(userId.value()).isPresent();
    }
}
