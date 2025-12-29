package app.dodb.guessimate.lobby.api.event;

public record ReactionSetEvent(String sessionId, String userId, Emoji emoji) implements WebSocketEvent, LobbyEvent {
}
