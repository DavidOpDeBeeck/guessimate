package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent;
import app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent;
import app.dodb.guessimate.lobby.api.event.DeckSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimateClearedEvent;
import app.dodb.guessimate.lobby.api.event.EstimateSetEvent;
import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
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
import java.util.Optional;

import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATION_COMPLETED;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class LobbyData {

    private DeckTO deck;
    private boolean autoReveal;
    private UserRole autoJoinRole;
    private TimerDuration timerDuration;
    private boolean reactionsEnabled;
    private String previousEstimationId;
    private Instant timerExpiresAt;
    private LobbyStatus status;
    private List<UserData> users;

    public LobbyData() {
    }

    public LobbyData(LobbyData.Builder builder) {
        this.deck = requireNonNull(builder.deck);
        this.autoReveal = requireNonNull(builder.autoReveal);
        this.autoJoinRole = builder.autoJoinRole;
        this.timerDuration = requireNonNull(builder.timerDuration);
        this.reactionsEnabled = requireNonNull(builder.reactionsEnabled);
        this.previousEstimationId = builder.previousEstimationId;
        this.timerExpiresAt = builder.timerExpiresAt;
        this.status = requireNonNull(builder.status);
        this.users = requireNonNull(builder.users);
    }

    void apply(DeckSetEvent event) {
        this.deck = event.deck();
    }

    void apply(AutoRevealEnabledEvent event) {
        this.autoReveal = true;
    }

    void apply(AutoRevealDisabledEvent event) {
        this.autoReveal = false;
    }

    void apply(AutoJoinUpdatedEvent event) {
        this.autoJoinRole = event.role();
    }

    void apply(TimerDurationSetEvent event) {
        this.timerDuration = event.timerDuration();
    }

    void apply(ReactionsEnabledEvent event) {
        this.reactionsEnabled = true;
    }

    void apply(ReactionsDisabledEvent event) {
        this.reactionsEnabled = false;
    }

    void apply(EstimationStartedEvent event) {
        this.status = ESTIMATING;
        this.timerExpiresAt = event.timerExpiresAt();
    }

    void apply(EstimationCompletedEvent event) {
        this.status = ESTIMATION_COMPLETED;
        this.previousEstimationId = event.estimationId();
    }

    void apply(UserConnectedEvent event) {
        users.add(new UserData.Builder()
            .userId(event.userId())
            .username(event.username())
            .build());
    }

    void apply(UserDisconnectedEvent event) {
        users.removeIf(user -> user.getUserId().equals(event.userId()));
    }

    void apply(UserRoleSetEvent event) {
        findUser(event.userId()).ifPresent(user -> user.setRole(event.role()));
    }

    void apply(UsernameSetEvent event) {
        findUser(event.userId()).ifPresent(user -> user.setUsername(event.username()));
    }

    void apply(EstimateSetEvent event) {
        findUser(event.userId()).ifPresent(user -> user.setEstimate(event.estimate()));
    }

    void apply(EstimateClearedEvent event) {
        findUser(event.userId()).ifPresent(user -> user.setEstimate(null));
    }

    void apply(ReactionSetEvent event) {
        findUser(event.userId()).ifPresent(user -> user.setReaction(event.emoji()));
    }

    void apply(ReactionClearedEvent event) {
        findUser(event.userId()).ifPresent(user -> user.setReaction(null));
    }

    private Optional<UserData> findUser(String userId) {
        return users.stream().filter(user -> user.getUserId().equals(userId)).findFirst();
    }

    public DeckTO getDeck() {
        return deck;
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

    public Optional<String> getPreviousEstimationId() {
        return ofNullable(previousEstimationId);
    }

    public Optional<Instant> getTimerExpiresAt() {
        return ofNullable(timerExpiresAt);
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public List<UserData> getUsers() {
        return users;
    }

    public static final class Builder {

        private DeckTO deck;
        private Boolean autoReveal;
        private UserRole autoJoinRole;
        private TimerDuration timerDuration;
        private Boolean reactionsEnabled;
        private String previousEstimationId;
        private Instant timerExpiresAt;
        private LobbyStatus status;
        private List<UserData> users;

        public Builder deck(DeckTO deck) {
            this.deck = deck;
            return this;
        }

        public Builder autoReveal(Boolean autoReveal) {
            this.autoReveal = autoReveal;
            return this;
        }

        public Builder autoJoinRole(UserRole autoJoinRole) {
            this.autoJoinRole = autoJoinRole;
            return this;
        }

        public Builder timerDuration(TimerDuration timerDuration) {
            this.timerDuration = timerDuration;
            return this;
        }

        public Builder reactionsEnabled(Boolean reactionsEnabled) {
            this.reactionsEnabled = reactionsEnabled;
            return this;
        }

        public Builder previousEstimationId(String previousEstimationId) {
            this.previousEstimationId = previousEstimationId;
            return this;
        }

        public Builder timerExpiresAt(Instant timerExpiresAt) {
            this.timerExpiresAt = timerExpiresAt;
            return this;
        }

        public Builder status(LobbyStatus status) {
            this.status = status;
            return this;
        }

        public Builder users(List<UserData> users) {
            this.users = users;
            return this;
        }

        public LobbyData build() {
            return new LobbyData(this);
        }
    }
}
