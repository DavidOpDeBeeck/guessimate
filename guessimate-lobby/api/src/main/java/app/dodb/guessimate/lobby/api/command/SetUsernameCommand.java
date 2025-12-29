package app.dodb.guessimate.lobby.api.command;

public record SetUsernameCommand(String username) implements WebSocketCommand {
}
