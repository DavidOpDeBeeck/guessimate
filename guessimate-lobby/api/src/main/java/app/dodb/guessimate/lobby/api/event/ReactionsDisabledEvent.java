package app.dodb.guessimate.lobby.api.event;

public record ReactionsDisabledEvent(String sessionId) implements WebSocketEvent, LobbyEvent {
}

