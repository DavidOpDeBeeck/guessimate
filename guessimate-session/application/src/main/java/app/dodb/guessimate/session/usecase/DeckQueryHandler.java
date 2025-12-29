package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.api.query.FindAllDecksQuery;
import app.dodb.guessimate.session.api.query.FindDeckByNameQuery;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.guessimate.session.port.DeckQueryRepository;
import app.dodb.smd.api.query.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class DeckQueryHandler {

    private final DeckQueryRepository deckQueryRepository;

    public DeckQueryHandler(DeckQueryRepository deckQueryRepository) {
        this.deckQueryRepository = deckQueryRepository;
    }

    @QueryHandler
    public DeckTO handle(FindDefaultDeckQuery query) {
        return deckQueryRepository.find(query);
    }

    @QueryHandler
    public Set<DeckTO> handle(FindAllDecksQuery query) {
        return deckQueryRepository.find(query);
    }

    @QueryHandler
    public Optional<DeckTO> handle(FindDeckByNameQuery query) {
        return deckQueryRepository.find(query);
    }
}
