package app.dodb.guessimate.lobby.drivingadapter.ws;

import app.dodb.guessimate.lobby.api.command.WebSocketCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public record WebSocketContext(WebSocketSession webSocketSession, ObjectMapper objectMapper) {

    public static final String USERNAME = "username";
    public static final String SESSION_ID = "sessionId";

    public String userId() {
        return webSocketSession.getId();
    }

    public String sessionId() {
        return webSocketSession.getAttributes().get(SESSION_ID).toString();
    }

    public Optional<String> userName() {
        return ofNullable(webSocketSession.getAttributes().get(USERNAME))
            .map(Object::toString);
    }

    public void send(Object message) {
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            // ignore
        }
    }

    public WebSocketCommand receive(TextMessage message) {
        try {
            return objectMapper.readValue(message.getPayload(), WebSocketCommand.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
