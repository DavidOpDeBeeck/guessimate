package app.dodb.guessimate.lobby.api.event;

import java.time.Instant;

public record EstimationStartedEvent(String sessionId, Instant timerExpiresAt) implements WebSocketEvent, LobbyEvent {
}
