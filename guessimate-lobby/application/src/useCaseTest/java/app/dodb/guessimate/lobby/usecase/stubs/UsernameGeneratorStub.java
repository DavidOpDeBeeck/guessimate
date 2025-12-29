package app.dodb.guessimate.lobby.usecase.stubs;

import app.dodb.guessimate.lobby.port.UsernameGenerator;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

@Component
@SMDTestScope
public class UsernameGeneratorStub implements UsernameGenerator {

    private String nextUsername = "default-username";

    public void stubNextUsername(String username) {
        this.nextUsername = username;
    }

    @Override
    public String generate() {
        return nextUsername;
    }
}
