package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.api.event.Emoji;
import app.dodb.guessimate.lobby.api.event.UserRole;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class UserData {

    private String userId;
    private String username;
    private String estimate;
    private Emoji reaction;
    private UserRole role;

    public UserData() {
    }

    public UserData(UserData.Builder builder) {
        this.userId = requireNonNull(builder.userId);
        this.username = requireNonNull(builder.username);
        this.estimate = builder.estimate;
        this.reaction = builder.reaction;
        this.role = builder.role;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public void setReaction(Emoji reaction) {
        this.reaction = reaction;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getEstimate() {
        return Optional.ofNullable(estimate);
    }

    public Optional<Emoji> getReaction() {
        return Optional.ofNullable(reaction);
    }

    public Optional<UserRole> getRole() {
        return Optional.ofNullable(role);
    }

    public static final class Builder {

        private String userId;
        private String username;
        private String estimate;
        private Emoji reaction;
        private UserRole role;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder estimate(String estimate) {
            this.estimate = estimate;
            return this;
        }

        public Builder reaction(Emoji reaction) {
            this.reaction = reaction;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public UserData build() {
            return new UserData(this);
        }
    }
}