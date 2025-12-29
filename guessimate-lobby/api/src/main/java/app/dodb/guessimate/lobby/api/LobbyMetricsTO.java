package app.dodb.guessimate.lobby.api;

public record LobbyMetricsTO(
    long activeLobbies,
    long connectedUsers,
    long completedEstimations
) {
}
