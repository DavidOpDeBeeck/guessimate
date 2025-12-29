package app.dodb.guessimate.lobby.api.command;

public record UserActionCommand<C extends WebSocketCommand>(String sessionId, String userId, C command) implements LobbyCommand {
}
