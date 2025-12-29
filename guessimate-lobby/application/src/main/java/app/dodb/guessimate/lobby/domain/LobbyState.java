package app.dodb.guessimate.lobby.domain;

import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.KeepAliveEvent;
import app.dodb.guessimate.lobby.api.event.LobbyEvent;
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

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATION_COMPLETED;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class LobbyState {

    private final SessionId sessionId;
    private DeckTO deck;
    private boolean autoReveal;
    private UserRole autoJoinRole;
    private boolean reactionsEnabled;
    private TimerDuration timerDuration;
    private Instant timerExpiresAt;
    private EstimationId previousEstimationId;
    private LobbyStatus status;
    private final List<User> users;

    public LobbyState(SessionId sessionId,
                      DeckTO deck,
                      boolean autoReveal,
                      UserRole autoJoinRole,
                      TimerDuration timerDuration,
                      boolean reactionsEnabled,
                      EstimationId previousEstimationId,
                      Instant timerExpiresAt,
                      LobbyStatus status,
                      List<User> users) {
        this.sessionId = requireNonNull(sessionId);
        this.deck = requireNonNull(deck);
        this.autoReveal = autoReveal;
        this.autoJoinRole = autoJoinRole;
        this.timerDuration = requireNonNull(timerDuration);
        this.reactionsEnabled = reactionsEnabled;
        this.previousEstimationId = previousEstimationId;
        this.timerExpiresAt = timerExpiresAt;
        this.status = requireNonNull(status);
        this.users = requireNonNull(users);
    }

    void apply(UserConnectedEvent event) {
        users.add(new User(new UserId(event.userId()), event.username()));
    }

    void apply(UserDisconnectedEvent event) {
        var user = findUserBy(new UserId(event.userId()));
        users.remove(user);
    }

    void apply(UserRoleSetEvent event) {
        var user = findUserBy(new UserId(event.userId()));
        user.setRole(event.role());
    }

    void apply(EstimateClearedEvent event) {
        var user = findUserBy(new UserId(event.userId()));
        user.clearEstimate();
    }

    void apply(UsernameSetEvent event) {
        var user = findUserBy(new UserId(event.userId()));
        user.setUsername(event.username());
    }

    void apply(EstimateSetEvent event) {
        var user = findUserBy(new UserId(event.userId()));
        user.setEstimate(event.estimate());
    }

    void apply(EstimationStartedEvent event) {
        status = ESTIMATING;
        timerExpiresAt = event.timerExpiresAt();
    }

    void apply(EstimationCompletedEvent event) {
        status = ESTIMATION_COMPLETED;
        previousEstimationId = new EstimationId(event.estimationId());
    }

    void apply(DeckSetEvent event) {
        deck = event.deck();
    }

    void apply(AutoJoinUpdatedEvent event) {
        autoJoinRole = event.role();
    }

    void apply(AutoRevealEnabledEvent event) {
        autoReveal = true;
    }

    void apply(AutoRevealDisabledEvent event) {
        autoReveal = false;
    }

    void apply(TimerDurationSetEvent event) {
        timerDuration = event.timerDuration();
    }

    void apply(ReactionsEnabledEvent event) {
        reactionsEnabled = true;
    }

    void apply(ReactionsDisabledEvent event) {
        reactionsEnabled = false;
    }

    void apply(ReactionSetEvent event) {
        var user = findUserBy(new UserId(event.userId()));
        user.setReaction(event.emoji());
    }

    void apply(ReactionClearedEvent event) {
        var user = findUserBy(new UserId(event.userId()));
        user.clearReaction();
    }

    void apply(KeepAliveEvent event) {
        // do nothing for now
    }

    public void apply(LobbyEvent event) {
        switch (event) {
            case UserConnectedEvent e -> apply(e);
            case UserDisconnectedEvent e -> apply(e);
            case UserRoleSetEvent e -> apply(e);
            case EstimateClearedEvent e -> apply(e);
            case UsernameSetEvent e -> apply(e);
            case EstimateSetEvent e -> apply(e);
            case EstimationStartedEvent e -> apply(e);
            case EstimationCompletedEvent e -> apply(e);
            case DeckSetEvent e -> apply(e);
            case AutoJoinUpdatedEvent e -> apply(e);
            case AutoRevealEnabledEvent e -> apply(e);
            case AutoRevealDisabledEvent e -> apply(e);
            case TimerDurationSetEvent e -> apply(e);
            case ReactionsEnabledEvent e -> apply(e);
            case ReactionsDisabledEvent e -> apply(e);
            case ReactionSetEvent e -> apply(e);
            case ReactionClearedEvent e -> apply(e);
            case KeepAliveEvent e -> apply(e);
        }
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public DeckTO getDeck() {
        return deck;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<UserId> getUserIds() {
        return users.stream()
            .map(User::userId)
            .toList();
    }

    public Optional<EstimationId> getPreviousEstimationId() {
        return ofNullable(previousEstimationId);
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public boolean isAutoReveal() {
        return autoReveal;
    }

    public Optional<UserRole> getAutoJoinRole() {
        return ofNullable(autoJoinRole);
    }

    public TimerDuration getTimerDuration() {
        return timerDuration;
    }

    public boolean isReactionsEnabled() {
        return reactionsEnabled;
    }

    public Optional<Instant> getTimerExpiresAt() {
        return ofNullable(timerExpiresAt);
    }

    public User findUserBy(UserId userId) {
        return users.stream()
            .filter(user -> user.hasUserId(userId))
            .findFirst()
            .orElseThrow();
    }

    public boolean allEstimatorsHaveVoted() {
        List<User> estimators = users.stream()
            .filter(User::isEstimator)
            .toList();
        if (estimators.isEmpty()) {
            return false;
        }
        return estimators.stream()
            .allMatch(user -> user.estimate().isPresent());
    }

    public boolean isTimerExpired() {
        return timerExpiresAt != null && Instant.now().isAfter(timerExpiresAt);
    }

    public boolean hasAutoJoinRole(UserRole role) {
        return Objects.equals(this.autoJoinRole, role);
    }
}