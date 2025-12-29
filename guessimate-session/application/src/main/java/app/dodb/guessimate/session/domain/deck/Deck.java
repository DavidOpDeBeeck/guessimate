package app.dodb.guessimate.session.domain.deck;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.domain.Estimate;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

public record Deck(DeckName deckName, List<Card> cards) {

    public static Deck from(DeckTO deck) {
        return new Deck(new DeckName(deck.deckName()), deck.cards().stream()
            .map(Card::new)
            .toList());
    }

    public Deck {
        requireNonNull(deckName);
        requireNonNull(cards);
    }

    public boolean exists(Estimate estimate) {
        return cards.stream().anyMatch(card -> card.value().equals(estimate.value()));
    }

    public int indexOf(Estimate estimate) {
        return cards.indexOf(new Card(estimate.value()));
    }

    public Comparator<Estimate> comparator() {
        return comparing(estimate -> cards.indexOf(new Card(estimate.value())));
    }

    public DeckTO asDeckTO() {
        return new DeckTO(deckName.value(), cards.stream().map(Card::value).toList());
    }
}
