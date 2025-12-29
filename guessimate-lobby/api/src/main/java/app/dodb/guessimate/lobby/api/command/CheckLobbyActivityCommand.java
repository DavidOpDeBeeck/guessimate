package app.dodb.guessimate.lobby.api.command;

public record CheckLobbyActivityCommand(String sessionId) implements LobbyCommand {
}
