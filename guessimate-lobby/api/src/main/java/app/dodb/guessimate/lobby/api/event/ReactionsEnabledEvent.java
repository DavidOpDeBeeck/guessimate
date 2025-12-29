package app.dodb.guessimate.lobby.api.event;

public record ReactionsEnabledEvent(String sessionId) implements WebSocketEvent, LobbyEvent {
}

