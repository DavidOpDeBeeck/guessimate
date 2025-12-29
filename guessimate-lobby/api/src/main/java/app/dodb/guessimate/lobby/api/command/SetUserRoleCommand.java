package app.dodb.guessimate.lobby.api.command;

import app.dodb.guessimate.lobby.api.event.UserRole;

public record SetUserRoleCommand(UserRole role) implements WebSocketCommand {
}
