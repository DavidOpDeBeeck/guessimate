package app.dodb.guessimate.lobby.api.event;

public record UserDisconnectedEvent(String sessionId, String userId) implements WebSocketEvent, LobbyEvent {
}
