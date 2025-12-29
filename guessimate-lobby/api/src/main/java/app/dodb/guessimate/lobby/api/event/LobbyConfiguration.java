package app.dodb.guessimate.lobby.api.event;

import app.dodb.guessimate.session.api.deck.DeckTO;

public record LobbyConfiguration(DeckTO deck,
                                 boolean autoReveal,
                                 UserRole autoJoinRole,
                                 TimerDuration timerDuration,
                                 boolean reactionsEnabled) {
}
