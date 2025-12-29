package app.dodb.guessimate.session.drivenadapter.deckview;

import app.dodb.guessimate.session.api.deck.DeckTO;
import app.dodb.guessimate.session.api.query.FindAllDecksQuery;
import app.dodb.guessimate.session.api.query.FindDeckByNameQuery;
import app.dodb.guessimate.session.api.query.FindDefaultDeckQuery;
import app.dodb.guessimate.session.port.DeckQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

public interface DeckViewSpringRepository extends JpaRepository<DeckView, Long>, DeckQueryRepository {

    Optional<DeckView> findByName(String name);

    Optional<DeckView> findByIsDefaultTrue();

    Set<DeckView> findAllByOrderByDisplayOrderAsc();

    @Override
    default DeckTO find(FindDefaultDeckQuery query) {
        return findByIsDefaultTrue()
            .map(DeckView::toTO)
            .orElseThrow(() -> new IllegalStateException("No default deck configured"));
    }

    @Override
    default Optional<DeckTO> find(FindDeckByNameQuery query) {
        return findByName(query.deckName())
            .map(DeckView::toTO);
    }

    @Override
    default Set<DeckTO> find(FindAllDecksQuery query) {
        return findAllByOrderByDisplayOrderAsc().stream()
            .map(DeckView::toTO)
            .collect(toCollection(LinkedHashSet::new));
    }
}
