package app.dodb.guessimate.lobby.api.event;

public record UsernameSetEvent(String sessionId, String userId, String username) implements WebSocketEvent, LobbyEvent {
}

