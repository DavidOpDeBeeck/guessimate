package app.dodb.guessimate.session.application;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@Target(TYPE)
@Retention(RUNTIME)
@EnableAutoConfiguration
@SpringBootTest(classes = GuessimateSessionConfiguration.class, webEnvironment = RANDOM_PORT)
public @interface AcceptanceTest {
}