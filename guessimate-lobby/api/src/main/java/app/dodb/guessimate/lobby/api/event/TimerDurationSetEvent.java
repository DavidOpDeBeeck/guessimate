package app.dodb.guessimate.lobby.api.event;

public record TimerDurationSetEvent(String sessionId, TimerDuration timerDuration) implements WebSocketEvent, LobbyEvent {
}

