package app.dodb.guessimate.lobby.drivingadapter.timer;

import app.dodb.guessimate.lobby.api.command.TryCompleteEstimationCommand;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.smd.api.command.CommandGateway;
import app.dodb.smd.api.event.EventHandler;
import app.dodb.smd.api.event.ProcessingGroup;
import app.dodb.smd.api.metadata.Metadata;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;

import static java.time.Duration.between;
import static java.time.ZoneId.systemDefault;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
@ProcessingGroup("estimation_timer_process_manager")
public class EstimationTimerProcessManager {

    private static final ScheduledExecutorService scheduler = newScheduledThreadPool(5);

    private final CommandGateway commandGateway;

    public EstimationTimerProcessManager(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @EventHandler
    public void on(EstimationStartedEvent event, Metadata metadata) {
        var sessionId = event.sessionId();
        var timerExpiresAt = event.timerExpiresAt();

        if (timerExpiresAt == null) {
            return;
        }

        var timeOfEvent = metadata.timestamp().atZone(systemDefault()).toInstant();
        var waitTimeInMillis = Math.max(0, between(timeOfEvent, event.timerExpiresAt()).toMillis());

        scheduler.schedule(() -> commandGateway.send(new TryCompleteEstimationCommand(sessionId)), waitTimeInMillis, MILLISECONDS);
    }
}

