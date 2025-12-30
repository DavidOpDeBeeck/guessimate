package app.dodb.guessimate.session.api.query;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.smd.api.query.Query;

import java.util.Optional;

public record FindDeckByNameQuery(String name) implements Query<Optional<DeckTO>> {
}
