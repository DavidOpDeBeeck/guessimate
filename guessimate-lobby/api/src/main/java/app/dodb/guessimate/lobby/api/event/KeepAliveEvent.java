package app.dodb.guessimate.lobby.api.event;

public record KeepAliveEvent(String sessionId) implements WebSocketEvent, LobbyEvent {
}
