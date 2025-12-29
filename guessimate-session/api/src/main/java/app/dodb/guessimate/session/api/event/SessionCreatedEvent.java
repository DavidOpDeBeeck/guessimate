package app.dodb.guessimate.session.api.event;

public record SessionCreatedEvent(String sessionId) implements SessionEvent {
}
