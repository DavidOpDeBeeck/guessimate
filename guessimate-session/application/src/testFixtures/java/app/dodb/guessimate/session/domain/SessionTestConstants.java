package app.dodb.guessimate.session.domain;

import app.dodb.guessimate.session.domain.deck.Card;
import app.dodb.guessimate.session.domain.deck.Deck;
import app.dodb.guessimate.session.domain.deck.DeckName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static app.dodb.guessimate.session.api.SessionApiTestConstants.ANOTHER_ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.DECK_NAME_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATE_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.ESTIMATION_ID_VALUE;
import static app.dodb.guessimate.session.api.SessionApiTestConstants.SESSION_ID_VALUE;

public class SessionTestConstants {

    public static final SessionId SESSION_ID = new SessionId(SESSION_ID_VALUE);
    public static final EstimationId ESTIMATION_ID = new EstimationId(ESTIMATION_ID_VALUE);
    public static final DeckName DECK_NAME = new DeckName(DECK_NAME_VALUE);

    public static EstimationId randomEstimationId() {
        return new EstimationId(UUID.randomUUID().toString());
    }

    public static Estimation anEstimation() {
        return new Estimation(ESTIMATION_ID, LocalDateTime.now(), List.of(anEstimate(), anotherEstimate()), aDeck());
    }

    public static Deck aDeck() {
        return new Deck(DECK_NAME, List.of(new Card("1"), new Card("2")));
    }

    public static Deck modifiedFibonacci() {
        return new Deck(
            new DeckName("Modified Fibonacci"),
            List.of(
                new Card("0"),
                new Card("0.5"),
                new Card("1"),
                new Card("2"),
                new Card("3"),
                new Card("5"),
                new Card("8"),
                new Card("13")
            )
        );
    }

    public static Estimate anEstimate() {
        return new Estimate(ESTIMATE_VALUE);
    }

    public static Estimate anotherEstimate() {
        return new Estimate(ANOTHER_ESTIMATE_VALUE);
    }
}
