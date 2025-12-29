package app.dodb.guessimate.lobby.drivenadapter.lobbymetricsview;

import app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent;
import app.dodb.guessimate.lobby.api.event.EstimationStartedEvent;
import app.dodb.guessimate.lobby.api.event.LobbyStatus;
import app.dodb.guessimate.lobby.api.event.UserConnectedEvent;
import app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.Instant;

import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATING;
import static app.dodb.guessimate.lobby.api.event.LobbyStatus.ESTIMATION_COMPLETED;
import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "lobby_metrics_view")
public class LobbyMetricsView implements Serializable {

    @Id
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "connected_user_count")
    private int connectedUserCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LobbyStatus status;

    @Column(name = "last_activity")
    private Instant lastActivity;

    @Column(name = "estimations_completed")
    private int estimationsCompleted;

    public LobbyMetricsView() {
    }

    public LobbyMetricsView(String sessionId, Instant createdAt) {
        this.sessionId = requireNonNull(sessionId);
        this.createdAt = requireNonNull(createdAt);
        this.lastActivity = requireNonNull(createdAt);
        this.connectedUserCount = 0;
        this.estimationsCompleted = 0;
    }

    private LobbyMetricsView(Builder builder) {
        this.sessionId = requireNonNull(builder.sessionId);
        this.createdAt = requireNonNull(builder.createdAt);
        this.lastActivity = builder.lastActivity != null ? builder.lastActivity : builder.createdAt;
        this.connectedUserCount = builder.connectedUserCount;
        this.status = builder.status;
        this.estimationsCompleted = builder.estimationsCompleted;
    }

    void apply(UserConnectedEvent event, Instant timestamp) {
        connectedUserCount++;
        lastActivity = requireNonNull(timestamp);
    }

    void apply(UserDisconnectedEvent event, Instant timestamp) {
        connectedUserCount--;
        lastActivity = requireNonNull(timestamp);
    }

    void apply(EstimationStartedEvent event, Instant timestamp) {
        status = ESTIMATING;
        lastActivity = requireNonNull(timestamp);
    }

    void apply(EstimationCompletedEvent event, Instant timestamp) {
        status = ESTIMATION_COMPLETED;
        estimationsCompleted++;
        lastActivity = requireNonNull(timestamp);
    }

    void updateLastActivity(Instant timestamp) {
        this.lastActivity = requireNonNull(timestamp);
    }

    public String getSessionId() {
        return sessionId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public int getConnectedUserCount() {
        return connectedUserCount;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public Instant getLastActivity() {
        return lastActivity;
    }

    public int getEstimationsCompleted() {
        return estimationsCompleted;
    }

    public static final class Builder {

        private String sessionId;
        private Instant createdAt;
        private Instant lastActivity;
        private int connectedUserCount;
        private LobbyStatus status;
        private int estimationsCompleted;

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastActivity(Instant lastActivity) {
            this.lastActivity = lastActivity;
            return this;
        }

        public Builder connectedUserCount(int connectedUserCount) {
            this.connectedUserCount = connectedUserCount;
            return this;
        }

        public Builder status(LobbyStatus status) {
            this.status = status;
            return this;
        }

        public Builder estimationsCompleted(int estimationsCompleted) {
            this.estimationsCompleted = estimationsCompleted;
            return this;
        }

        public LobbyMetricsView build() {
            return new LobbyMetricsView(this);
        }
    }
}
