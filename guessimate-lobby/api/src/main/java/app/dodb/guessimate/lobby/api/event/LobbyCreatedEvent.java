package app.dodb.guessimate.lobby.api.event;

public record LobbyCreatedEvent(String sessionId) implements LobbyEvent {
}
