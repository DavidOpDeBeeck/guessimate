package app.dodb.guessimate.application;

import app.dodb.guessimate.lobby.application.GuessimateLobbyConfiguration;
import app.dodb.guessimate.session.application.GuessimateSessionConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import({
    GuessimateSessionConfiguration.class,
    GuessimateLobbyConfiguration.class,
})
public class GuessimateApplication {

    static void main(String[] args) {
        SpringApplication.run(GuessimateApplication.class, args);
    }
}


