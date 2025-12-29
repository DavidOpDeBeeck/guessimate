package app.dodb.guessimate.lobby;

import app.dodb.guessimate.lobby.drivenadapter.GuessimateLobbyDrivenAdapterConfiguration;
import app.dodb.guessimate.lobby.drivingadapter.GuessimateLobbyDrivingAdapterConfiguration;
import app.dodb.guessimate.lobby.usecase.GuessimateLobbyUseCaseConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(TYPE)
@Retention(RUNTIME)
@EnableAutoConfiguration
@Transactional
@Rollback
@SpringBootTest(classes = {
    GuessimateLobbyDrivingAdapterConfiguration.class,
    GuessimateLobbyUseCaseConfiguration.class,
    GuessimateLobbyDrivenAdapterConfiguration.class
})
public @interface IntegrationTest {
}
