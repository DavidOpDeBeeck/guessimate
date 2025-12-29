package app.dodb.guessimate.session.drivenadapter.sessionview;

import app.dodb.guessimate.session.api.EstimationTO;
import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.api.event.EstimationRemovedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Table(name = "session_view")
public class SessionView implements Serializable {

    @Id
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "data")
    @JdbcTypeCode(JSON)
    private SessionViewData data;

    public SessionView() {
    }

    public SessionView(String sessionId, SessionViewData data) {
        this.sessionId = requireNonNull(sessionId);
        this.data = requireNonNull(data);
    }

    void apply(EstimationAddedEvent event) {
        data.apply(event);
    }

    void apply(EstimationRemovedEvent event) {
        data.apply(event);
    }

    public String getSessionId() {
        return sessionId;
    }

    public Set<EstimationTO> getEstimations() {
        return data.getEstimations();
    }
}
