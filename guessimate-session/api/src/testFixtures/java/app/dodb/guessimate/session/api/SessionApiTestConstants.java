package app.dodb.guessimate.session.api;

import app.dodb.guessimate.session.api.deck.DeckTO;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

public class SessionApiTestConstants {

    public static final String SESSION_ID_VALUE = "session-id";
    public static final String ANOTHER_SESSION_ID_VALUE = "another-session-id";
    public static final String NON_EXISTENT_SESSION_ID_VALUE = "non-existent-session-id";
    public static final String ESTIMATION_ID_VALUE = "estimation-id";
    public static final String ESTIMATE_VALUE = "1";
    public static final String ANOTHER_ESTIMATE_VALUE = "2";
    public static final String ESTIMATE_NOT_IN_DECK_VALUE = "3";

    public static final String DECK_NAME_VALUE = "deck";
    public static final String ANOTHER_DECK_NAME_VALUE = "another-deck";
    public static final String NON_EXISTENT_DECK_NAME_VALUE = "non-existent-deck";

    public static EstimationTO anEstimationTO() {
        return new EstimationTO(ESTIMATION_ID_VALUE, LocalDateTime.now(), aDeckTO(), List.of(ESTIMATE_VALUE, ANOTHER_ESTIMATE_VALUE), emptySet(), 0, emptyMap());
    }

    public static DeckTO aDeckTO() {
        return new DeckTO(DECK_NAME_VALUE, List.of("1", "2"));
    }

    public static DeckTO anotherDeckTO() {
        return new DeckTO(ANOTHER_DECK_NAME_VALUE, List.of("S", "M", "L"));
    }
}
