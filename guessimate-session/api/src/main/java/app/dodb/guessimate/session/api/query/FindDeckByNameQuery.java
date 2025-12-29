package app.dodb.guessimate.session.api.query;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.smd.api.query.Query;

import java.util.Optional;

public record FindDeckByNameQuery(String deckName) implements Query<Optional<DeckTO>> {
}
