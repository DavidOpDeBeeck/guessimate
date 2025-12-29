package app.dodb.guessimate.lobby.api.event;

public record UserConnectedEvent(String sessionId, String userId, String username) implements WebSocketEvent, LobbyEvent {
}
