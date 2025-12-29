package app.dodb.guessimate.lobby.api.event;

public record AutoRevealEnabledEvent(String sessionId) implements WebSocketEvent, LobbyEvent {
}

