package app.dodb.guessimate.lobby.api.command;

public record DisconnectUserFromLobbyCommand(String sessionId, String userId) implements LobbyCommand {
}
