package app.dodb.guessimate.session.usecase.stubs;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.api.query.FindAllDecksQuery;
import app.dodb.guessimate.session.api.query.FindDeckByNameQuery;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.guessimate.session.port.DeckQueryRepository;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@SMDTestScope
public class DeckQueryRepositoryStub implements DeckQueryRepository {

    @Override
    public DeckTO find(FindDefaultDeckQuery query) {
        return null;
    }

    @Override
    public Optional<DeckTO> find(FindDeckByNameQuery query) {
        return Optional.empty();
    }

    @Override
    public Set<DeckTO> find(FindAllDecksQuery query) {
        return Set.of();
    }
}
