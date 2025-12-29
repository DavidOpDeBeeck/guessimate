package app.dodb.guessimate.lobby.api.command;

public record SetEstimateCommand(String estimate) implements WebSocketCommand {
}
