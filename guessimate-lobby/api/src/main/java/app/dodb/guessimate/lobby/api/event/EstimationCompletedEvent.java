package app.dodb.guessimate.lobby.api.event;

import java.util.List;

public record EstimationCompletedEvent(String sessionId, String estimationId, List<String> estimates) implements WebSocketEvent, LobbyEvent {
}
