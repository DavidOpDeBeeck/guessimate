package app.dodb.guessimate.lobby.application;

import app.dodb.guessimate.lobby.drivenadapter.GuessimateLobbyDrivenAdapterConfiguration;
import app.dodb.guessimate.lobby.drivingadapter.GuessimateLobbyDrivingAdapterConfiguration;
import app.dodb.guessimate.lobby.usecase.GuessimateLobbyUseCaseConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    GuessimateLobbyDrivingAdapterConfiguration.class,
    GuessimateLobbyUseCaseConfiguration.class,
    GuessimateLobbyDrivenAdapterConfiguration.class
})
@ComponentScan
public class GuessimateLobbyConfiguration {
}
