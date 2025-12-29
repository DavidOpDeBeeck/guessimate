package app.dodb.guessimate.lobby.api.event;

public record ReactionClearedEvent(String sessionId, String userId) implements WebSocketEvent, LobbyEvent {
}
