package app.dodb.guessimate.lobby.api.command;

import app.dodb.guessimate.lobby.api.event.LobbyInfoSetEvent;

import java.util.Optional;

public record ConnectUserToLobbyCommand(String sessionId,
                                        String userId,
                                        Optional<String> username) implements LobbyCommand<LobbyInfoSetEvent> {
}
