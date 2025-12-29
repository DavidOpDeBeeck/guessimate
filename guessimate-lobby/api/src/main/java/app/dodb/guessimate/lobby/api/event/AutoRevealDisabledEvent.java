package app.dodb.guessimate.lobby.api.event;

public record AutoRevealDisabledEvent(String sessionId) implements WebSocketEvent, LobbyEvent {
}

