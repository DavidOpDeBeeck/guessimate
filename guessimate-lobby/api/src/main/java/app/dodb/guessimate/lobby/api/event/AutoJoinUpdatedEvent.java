package app.dodb.guessimate.lobby.api.event;

public record AutoJoinUpdatedEvent(String sessionId, UserRole role) implements WebSocketEvent, LobbyEvent {
}
