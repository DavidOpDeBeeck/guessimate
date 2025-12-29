package app.dodb.guessimate.session.api.query;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.smd.api.query.Query;

public record FindDefaultDeckQuery() implements Query<DeckTO> {
}
