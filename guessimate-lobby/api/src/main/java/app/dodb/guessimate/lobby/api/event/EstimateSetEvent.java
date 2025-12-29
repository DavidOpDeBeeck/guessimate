package app.dodb.guessimate.lobby.api.event;

public record EstimateSetEvent(String sessionId, String userId, String estimate) implements WebSocketEvent, LobbyEvent {
}
