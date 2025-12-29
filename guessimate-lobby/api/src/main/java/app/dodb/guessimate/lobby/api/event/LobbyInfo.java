package app.dodb.guessimate.lobby.api.event;

import java.util.List;

public record LobbyInfo(String sessionId,
                        LobbyConfiguration configuration,
                        String previousEstimationId,
                        LobbyStatus status,
                        List<UserInfo> users,
                        String timerExpiresAt) {
}
