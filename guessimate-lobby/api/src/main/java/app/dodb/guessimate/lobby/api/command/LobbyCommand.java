package app.dodb.guessimate.lobby.api.command;

import app.dodb.smd.api.command.Command;

public interface LobbyCommand<R> extends Command<R> {

    String sessionId();
}
