package app.dodb.guessimate.lobby.api.command;

public record SetAutoRevealCommand(boolean enabled) implements WebSocketCommand {
}

