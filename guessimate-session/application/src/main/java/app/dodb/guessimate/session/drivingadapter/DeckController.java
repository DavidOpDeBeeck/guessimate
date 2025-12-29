package app.dodb.guessimate.session.drivingadapter;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.api.query.FindAllDecksQuery;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class DeckController {

    private final QueryGateway queryGateway;

    public DeckController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/decks")
    public ResponseEntity<Set<DeckTO>> findAllDecks() {
        return ResponseEntity.ok(queryGateway.send(new FindAllDecksQuery()));
    }
}
