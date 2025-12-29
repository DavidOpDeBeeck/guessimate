package app.dodb.guessimate.lobby.drivingadapter.ws;

import app.dodb.guessimate.lobby.drivingadapter.ws.interceptor.SessionIdInterceptor;
import app.dodb.guessimate.lobby.drivingadapter.ws.interceptor.UsernameInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final LobbyWebSocketHandler lobbyWebSocketHandler;
    private final SessionIdInterceptor sessionIdInterceptor;
    private final UsernameInterceptor usernameInterceptor;

    public WebSocketConfiguration(LobbyWebSocketHandler lobbyWebSocketHandler,
                                  SessionIdInterceptor sessionIdInterceptor,
                                  UsernameInterceptor usernameInterceptor) {
        this.lobbyWebSocketHandler = lobbyWebSocketHandler;
        this.sessionIdInterceptor = sessionIdInterceptor;
        this.usernameInterceptor = usernameInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler(lobbyWebSocketHandler, "/ws/lobby/{sessionId}")
            .addInterceptors(sessionIdInterceptor, usernameInterceptor)
            .setAllowedOrigins("*");
    }
}
