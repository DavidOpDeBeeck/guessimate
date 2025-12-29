package app.dodb.guessimate.lobby.port;

import app.dodb.guessimate.lobby.domain.UserId;

public interface UserConnectivityChecker {

    boolean isConnected(UserId userId);
}
