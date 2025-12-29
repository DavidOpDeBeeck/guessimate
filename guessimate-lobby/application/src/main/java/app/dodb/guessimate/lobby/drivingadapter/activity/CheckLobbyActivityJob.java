package app.dodb.guessimate.lobby.drivingadapter.activity;

import app.dodb.guessimate.lobby.api.command.CheckLobbyActivityCommand;
import app.dodb.guessimate.lobby.api.query.FindActiveLobbiesQuery;
import app.dodb.smd.api.command.CommandGateway;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckLobbyActivityJob {

    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    public CheckLobbyActivityJob(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void schedule() {
        queryGateway.send(new FindActiveLobbiesQuery()).forEach(sessionId ->
            commandGateway.send(new CheckLobbyActivityCommand(sessionId))
        );
    }
}
