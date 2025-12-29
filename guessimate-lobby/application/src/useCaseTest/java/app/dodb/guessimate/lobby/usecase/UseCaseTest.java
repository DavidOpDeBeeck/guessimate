package app.dodb.guessimate.lobby.usecase;

import app.dodb.smd.spring.test.EnableSMDStubs;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@EnableSMDStubs
@SpringBootTest(classes = GuessimateLobbyUseCaseConfiguration.class)
public @interface UseCaseTest {
}
