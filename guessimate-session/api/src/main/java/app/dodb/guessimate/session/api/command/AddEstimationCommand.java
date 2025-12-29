package app.dodb.guessimate.session.api.command;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.smd.api.command.Command;

import java.util.List;

public record AddEstimationCommand(String sessionId, DeckTO deck, List<String> estimates) implements Command<String> {
}
