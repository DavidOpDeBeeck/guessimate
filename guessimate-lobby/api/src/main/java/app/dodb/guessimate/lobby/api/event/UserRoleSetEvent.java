package app.dodb.guessimate.lobby.api.event;

public record UserRoleSetEvent(String sessionId, String userId, UserRole role) implements WebSocketEvent, LobbyEvent {
}
