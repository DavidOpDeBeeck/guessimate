package app.dodb.guessimate.session.domain.deck;

import static java.util.Objects.requireNonNull;

public record DeckName(String value) {

    public DeckName {
        requireNonNull(value);
    }
}
