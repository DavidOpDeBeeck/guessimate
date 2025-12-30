package app.dodb.guessimate.session.domain.deck;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.domain.Estimate;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

public record Deck(DeckName name, List<String> cards) {

    public static Deck from(DeckTO deck) {
        return new Deck(new DeckName(deck.deckName()), deck.cards());
    }

    public Deck {
        requireNonNull(name);
        requireNonNull(cards);
    }

    public boolean exists(Estimate estimate) {
        return cards.stream().anyMatch(card -> card.equals(estimate.value()));
    }

    public int indexOf(Estimate estimate) {
        return cards.indexOf(estimate.value());
    }

    public Comparator<Estimate> comparator() {
        return comparing(estimate -> cards.indexOf(estimate.value()));
    }

    public DeckTO asDeckTO() {
        return new DeckTO(name.value(), cards);
    }
}
