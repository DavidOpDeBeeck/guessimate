package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyCreatedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
import app.dodb.guessimate.lobby.api.event.ReactionClearedEvent;
import app.dodb.guessimate.lobby.api.event.ReactionSetEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsDisabledEvent;
import app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent;
import app.dodb.guessimate.lobby.api.event.TimerDuration;
import app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserRole;
import app.dodb.guessimate.lobby.api.event.UserRoleSetEvent;
import app.dodb.guessimate.lobby.api.event.UsernameSetEvent;
import app.dodb.guessimate.session.api.deck.DeckTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Table(name = "lobby_view")
public class LobbyView implements Serializable {

    @Id
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "data")
    @JdbcTypeCode(JSON)
    private LobbyData data;

    public LobbyView() {
    }

    public LobbyView(String sessionId, LobbyData data) {
        this.sessionId = requireNonNull(sessionId);
        this.data = requireNonNull(data);
    }

    void apply(DeckSetEvent event) {
        this.data = data.apply(event);
    }

    void apply(AutoRevealEnabledEvent event) {
        this.data = data.apply(event);
    }

    void apply(AutoRevealDisabledEvent event) {
        this.data = data.apply(event);
    }

    void apply(AutoJoinUpdatedEvent event) {
        this.data = data.apply(event);
    }

    void apply(TimerDurationSetEvent event) {
        this.data = data.apply(event);
    }

    void apply(ReactionsEnabledEvent event) {
        this.data = data.apply(event);
    }

    void apply(ReactionsDisabledEvent event) {
        this.data = data.apply(event);
    }

    void apply(EstimationStartedEvent event) {
        this.data = data.apply(event);
    }

    void apply(EstimationCompletedEvent event) {
        this.data = data.apply(event);
    }

    void apply(UserConnectedEvent event) {
        this.data = data.apply(event);
    }

    void apply(UserDisconnectedEvent event) {
        this.data = data.apply(event);
    }

    void apply(UserRoleSetEvent event) {
        this.data = data.apply(event);
    }

    void apply(UsernameSetEvent event) {
        this.data = data.apply(event);
    }

    void apply(EstimateSetEvent event) {
        this.data = data.apply(event);
    }

    void apply(EstimateClearedEvent event) {
        this.data = data.apply(event);
    }

    void apply(ReactionSetEvent event) {
        this.data = data.apply(event);
    }

    void apply(ReactionClearedEvent event) {
        this.data = data.apply(event);
    }

    public String getSessionId() {
        return sessionId;
    }

    public DeckTO getDeck() {
        return data.deck();
    }

    public boolean isAutoReveal() {
        return data.autoReveal();
    }

    public Optional<UserRole> getAutoJoinRole() {
        return data.getAutoJoinRole();
    }

    public TimerDuration getTimerDuration() {
        return data.timerDuration();
    }

    public boolean isReactionsEnabled() {
        return data.reactionsEnabled();
    }

    public Optional<String> getPreviousEstimationId() {
        return data.getPreviousEstimationId();
    }

    public Optional<Instant> getTimerExpiresAt() {
        return data.getTimerExpiresAt();
    }

    public LobbyStatus getStatus() {
        return data.status();
    }

    public List<UserData> getUsers() {
        return data.users();
    }
}
