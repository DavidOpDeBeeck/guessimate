package app.dodb.guessimate.lobby.api.event;

import java.util.List;

public record LobbyInfoSetEvent(String sessionId,
                                LobbyConfiguration configuration,
                                String previousEstimationId,
                                LobbyStatus status,
                                List<UserInfo> users,
                                String timerExpiresAt) implements WebSocketEvent {

    public static LobbyInfoSetEvent from(LobbyInfo lobbyInfo) {
        return new LobbyInfoSetEvent(
            lobbyInfo.sessionId(),
            lobbyInfo.configuration(),
            lobbyInfo.previousEstimationId(),
            lobbyInfo.status(),
            lobbyInfo.users(),
            lobbyInfo.timerExpiresAt()
        );
    }
}
