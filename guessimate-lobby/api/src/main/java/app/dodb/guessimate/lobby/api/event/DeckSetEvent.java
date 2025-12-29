package app.dodb.guessimate.lobby.api.event;

import app.dodb.guessimate.session.api.deck.DeckTO;

public record DeckSetEvent(String sessionId, DeckTO deck) implements WebSocketEvent, LobbyEvent {
}
