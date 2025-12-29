package app.dodb.guessimate.session.api.deck;

import java.util.List;

public record DeckTO(String deckName, List<String> cards) {

    public boolean contains(String value) {
        return cards.contains(value);
    }
}
