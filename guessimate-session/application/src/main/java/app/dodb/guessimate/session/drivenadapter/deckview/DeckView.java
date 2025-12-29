package app.dodb.guessimate.session.drivenadapter.deckview;

import app.dodb.guessimate.session.api.deck.DeckTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Table(name = "deck_view")
public class DeckView {

    @Id
    private String name;
    @JdbcTypeCode(JSON)
    private List<String> cards;
    private boolean isDefault;
    private int displayOrder;

    public DeckView() {
    }

    public DeckView(String name, List<String> cards, Boolean isDefault, Integer displayOrder) {
        this.name = requireNonNull(name);
        this.cards = requireNonNull(cards);
        this.isDefault = requireNonNull(isDefault);
        this.displayOrder = requireNonNull(displayOrder);
    }

    public DeckTO toTO() {
        return new DeckTO(name, cards);
    }

    public String getName() {
        return name;
    }

    public List<String> getCards() {
        return cards;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
