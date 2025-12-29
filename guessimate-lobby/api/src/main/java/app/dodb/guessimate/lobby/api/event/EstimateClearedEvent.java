package app.dodb.guessimate.lobby.api.event;

public record EstimateClearedEvent(String sessionId, String userId) implements WebSocketEvent, LobbyEvent {
}
