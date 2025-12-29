package app.dodb.guessimate.lobby.api.command;

import app.dodb.smd.api.command.Command;

public interface LobbyCommand extends Command<Void> {

    String sessionId();
}
