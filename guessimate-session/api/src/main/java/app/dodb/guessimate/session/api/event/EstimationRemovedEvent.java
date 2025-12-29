package app.dodb.guessimate.session.api.event;

public record EstimationRemovedEvent(String sessionId, String estimationId) implements SessionEvent {
}
