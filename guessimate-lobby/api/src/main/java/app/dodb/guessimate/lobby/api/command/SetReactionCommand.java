package app.dodb.guessimate.lobby.api.command;

import app.dodb.guessimate.lobby.api.event.Emoji;

public record SetReactionCommand(Emoji emoji) implements WebSocketCommand {
}
