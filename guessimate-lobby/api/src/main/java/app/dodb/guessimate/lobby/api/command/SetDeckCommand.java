package app.dodb.guessimate.lobby.api.command;

public record SetDeckCommand(String deckName) implements WebSocketCommand {
}

