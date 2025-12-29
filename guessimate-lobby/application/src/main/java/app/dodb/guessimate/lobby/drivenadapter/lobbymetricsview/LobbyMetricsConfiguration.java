package app.dodb.guessimate.lobby.drivenadapter.lobbymetricsview;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;

@Configuration
public class LobbyMetricsConfiguration {

    @Autowired
    public void registerMetrics(MeterRegistry meterRegistry, LobbyMetricsViewSpringRepository repository) {
        Gauge.builder("guessimate.lobbies.active.total", repository, LobbyMetricsViewSpringRepository::countActiveLobbies)
            .description("Total number of active lobbies with connected users")
            .register(meterRegistry);

        Gauge.builder("guessimate.lobby.users.connected.total", repository, LobbyMetricsViewSpringRepository::countTotalConnectedUsers)
            .description("Total number of users connected across all lobbies")
            .register(meterRegistry);

        Gauge.builder("guessimate.estimations.active.total", repository, LobbyMetricsViewSpringRepository::countActiveEstimations)
            .description("Number of lobbies currently in ESTIMATING status")
            .register(meterRegistry);

        Gauge.builder("guessimate.estimations.completed.total", repository, LobbyMetricsViewSpringRepository::sumEstimationsCompleted)
            .description("Total number of estimations completed")
            .register(meterRegistry);

        Gauge.builder("guessimate.lobbies.abandoned.total", repository, LobbyMetricsViewSpringRepository::countAbandonedLobbies)
            .description("Number of lobbies with no connected users")
            .register(meterRegistry);

        Gauge.builder("guessimate.lobbies.idle.total", repository, r -> {
                Duration idleThreshold = Duration.ofMinutes(30);
                return r.countIdleLobbies(Instant.now().minus(idleThreshold));
            })
            .description("Number of lobbies with no activity for >30 minutes")
            .register(meterRegistry);
    }

}
