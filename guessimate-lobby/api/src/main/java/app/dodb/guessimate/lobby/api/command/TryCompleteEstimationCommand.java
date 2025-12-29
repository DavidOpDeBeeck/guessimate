package app.dodb.guessimate.lobby.api.command;

import app.dodb.smd.api.command.Command;

public record TryCompleteEstimationCommand(String sessionId) implements Command<Void> {
}
