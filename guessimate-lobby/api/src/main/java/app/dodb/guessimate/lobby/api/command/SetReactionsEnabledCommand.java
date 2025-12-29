package app.dodb.guessimate.lobby.api.command;

public record SetReactionsEnabledCommand(boolean enabled) implements WebSocketCommand {
}

