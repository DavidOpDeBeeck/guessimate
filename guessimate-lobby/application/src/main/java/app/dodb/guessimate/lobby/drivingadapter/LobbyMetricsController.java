package app.dodb.guessimate.lobby.drivingadapter;

import app.dodb.guessimate.lobby.api.LobbyMetricsTO;
import app.dodb.guessimate.lobby.api.query.FindLobbyMetricsQuery;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LobbyMetricsController {

    private final QueryGateway queryGateway;

    public LobbyMetricsController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/metrics")
    public ResponseEntity<LobbyMetricsTO> getLobbyMetrics() {
        return ResponseEntity.ok(queryGateway.send(new FindLobbyMetricsQuery()));
    }
}
