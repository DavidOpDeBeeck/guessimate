package app.dodb.guessimate.lobby.usecase.stubs;

import app.dodb.guessimate.lobby.domain.UserId;
import app.dodb.guessimate.lobby.port.UserConnectivityChecker;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@SMDTestScope
public class UserConnectivityCheckerStub implements UserConnectivityChecker {

    private final Set<UserId> connectedUsers = new HashSet<>();

    public void setConnected(UserId userId, boolean connected) {
        if (connected) {
            connectedUsers.add(userId);
        } else {
            connectedUsers.remove(userId);
        }
    }

    @Override
    public boolean isConnected(UserId userId) {
        return connectedUsers.contains(userId);
    }
}
