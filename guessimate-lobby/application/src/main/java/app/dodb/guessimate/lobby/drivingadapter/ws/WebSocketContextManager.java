package app.dodb.guessimate.lobby.drivingadapter.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Component
public class WebSocketContextManager {

    private final Map<String, WebSocketContext> webSocketContextByUserId = new ConcurrentHashMap<>();
    private final Map<String, List<String>> userIdsBySessionId = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;

    public WebSocketContextManager(@Qualifier("webSocketObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public WebSocketContext addSession(WebSocketSession session) {
        var context = new WebSocketContext(session, objectMapper);
        webSocketContextByUserId.put(context.userId(), context);
        return context;
    }

    public WebSocketContext removeSession(WebSocketSession session) {
        var context = new WebSocketContext(session, objectMapper);
        webSocketContextByUserId.remove(context.userId());
        return context;
    }

    public WebSocketContext getContextBy(WebSocketSession session) {
        return new WebSocketContext(session, objectMapper);
    }

    public List<WebSocketContext> getContextsBySessionId(String sessionId) {
        return userIdsBySessionId.getOrDefault(sessionId, emptyList()).stream()
            .map(this::getContextByUserId)
            .flatMap(Optional::stream)
            .toList();
    }

    public Optional<WebSocketContext> getContextByUserId(String userId) {
        return ofNullable(webSocketContextByUserId.get(userId));
    }

    public void link(String sessionId, String userId) {
        userIdsBySessionId.compute(sessionId, (_, userIds) -> {
            if (userIds == null) {
                userIds = new ArrayList<>();
            }
            userIds.add(userId);
            return userIds;
        });
    }

    public void unlink(String sessionId, String userId) {
        userIdsBySessionId.compute(sessionId, (_, userIds) -> {
            if (userIds == null) {
                return null;
            }
            userIds.remove(userId);
            return userIds.isEmpty() ? null : userIds;
        });
    }
}
