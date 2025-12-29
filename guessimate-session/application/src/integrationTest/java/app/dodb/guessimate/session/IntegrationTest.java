package app.dodb.guessimate.session;

import app.dodb.guessimate.session.drivenadapter.GuessimateDrivenAdapterConfiguration;
import app.dodb.guessimate.session.usecase.GuessimateUseCaseConfiguration;
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
@SpringBootTest(classes = {GuessimateUseCaseConfiguration.class, GuessimateDrivenAdapterConfiguration.class})
public @interface IntegrationTest {
}