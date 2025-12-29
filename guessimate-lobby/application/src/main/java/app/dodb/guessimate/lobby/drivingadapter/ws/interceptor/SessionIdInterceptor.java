package app.dodb.guessimate.lobby.drivingadapter.ws.interceptor;

import app.dodb.guessimate.session.api.query.FindSessionByIdQuery;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static app.dodb.guessimate.lobby.drivingadapter.ws.WebSocketContext.SESSION_ID;

@Component
public class SessionIdInterceptor implements HandshakeInterceptor {

    private static final Pattern PATTERN = Pattern.compile("/ws/lobby/(?<sessionId>[^/]+)");

    private final QueryGateway queryGateway;

    public SessionIdInterceptor(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String path = request.getURI().getPath();
        Matcher matcher = PATTERN.matcher(path);

        if (matcher.find()) {
            String sessionId = matcher.group("sessionId");
            attributes.put(SESSION_ID, sessionId);
            return queryGateway.send(new FindSessionByIdQuery(sessionId)).isPresent();
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}