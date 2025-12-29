package app.dodb.guessimate.lobby.api.command;

import app.dodb.guessimate.lobby.api.event.UserRole;

public record SetAutoJoinCommand(UserRole role) implements WebSocketCommand {
}
