package app.dodb.guessimate.session.drivingadapter;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.SessionTO;
import app.dodb.guessimate.session.api.command.CreateSessionCommand;
import app.dodb.guessimate.session.api.query.FindEstimationByIdQuery;
import app.dodb.guessimate.session.api.query.FindEstimationsBySessionIdQuery;
import app.dodb.guessimate.session.api.query.FindSessionByIdQuery;
import app.dodb.smd.api.command.CommandGateway;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class SessionController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public SessionController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/sessions")
    public ResponseEntity<SessionTO> createSession() {
        var sessionId = commandGateway.send(new CreateSessionCommand());
        return ResponseEntity.of(queryGateway.send(new FindSessionByIdQuery(sessionId)));
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionTO> findSessionById(@PathVariable String sessionId) {
        return ResponseEntity.of(queryGateway.send(new FindSessionByIdQuery(sessionId)));
    }

    @GetMapping("/sessions/{sessionId}/estimations")
    public ResponseEntity<Set<EstimationTO>> findEstimationsBySessionId(@PathVariable String sessionId) {
        return ResponseEntity.of(queryGateway.send(new FindEstimationsBySessionIdQuery(sessionId)));
    }

    @GetMapping("/sessions/{sessionId}/estimations/{estimationId}")
    public ResponseEntity<EstimationTO> findEstimationById(@PathVariable String sessionId, @PathVariable String estimationId) {
        return ResponseEntity.of(queryGateway.send(new FindEstimationByIdQuery(sessionId, estimationId)));
    }
}
