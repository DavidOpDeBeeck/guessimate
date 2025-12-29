package app.dodb.guessimate.session.application;

import app.dodb.guessimate.session.drivenadapter.GuessimateDrivenAdapterConfiguration;
import app.dodb.guessimate.session.drivingadapter.GuessimateDrivingAdapterConfiguration;
import app.dodb.guessimate.session.usecase.GuessimateUseCaseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    GuessimateDrivingAdapterConfiguration.class,
    GuessimateUseCaseConfiguration.class,
    GuessimateDrivenAdapterConfiguration.class,
})
public class GuessimateSessionConfiguration {
}
