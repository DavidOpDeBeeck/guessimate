package app.dodb.guessimate.session.port;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.api.query.FindAllDecksQuery;
import app.dodb.guessimate.session.api.query.FindDeckByNameQuery;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;

import java.util.Optional;
import java.util.Set;

public interface DeckQueryRepository {

    DeckTO find(FindDefaultDeckQuery query);

    Optional<DeckTO> find(FindDeckByNameQuery query);

    Set<DeckTO> find(FindAllDecksQuery query);
}
