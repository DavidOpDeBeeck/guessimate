package app.dodb.guessimate.lobby.domain;

import app.dodb.guessimate.lobby.api.event.Emoji;
import app.dodb.guessimate.lobby.api.event.UserRole;

import java.util.Objects;
import java.util.Optional;

import static app.dodb.guessimate.lobby.api.event.UserRole.ESTIMATOR;
import static app.dodb.guessimate.lobby.api.event.UserRole.OBSERVER;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class User {

    private final UserId userId;
    private String username;
    private String estimate;
    private Emoji reaction;
    private UserRole role;

    public User(UserId userId, String username) {
        this.userId = requireNonNull(userId);
        this.username = requireNonNull(username);
    }

    public User(UserId userId, String username, String estimate, Emoji reaction, UserRole role) {
        this(userId, username);
        this.estimate = estimate;
        this.reaction = reaction;
        this.role = role;
    }

    public UserId userId() {
        return userId;
    }

    public String username() {
        return username;
    }

    public Optional<String> estimate() {
        return ofNullable(estimate);
    }

    public Optional<Emoji> reaction() {
        return ofNullable(reaction);
    }

    public Optional<UserRole> role() {
        return ofNullable(role);
    }

    public void setUsername(String username) {
        this.username = requireNonNull(username);
    }

    public void setEstimate(String estimate) {
        this.estimate = requireNonNull(estimate);
    }

    public void setReaction(Emoji reaction) {
        this.reaction = requireNonNull(reaction);
    }

    public void setRole(UserRole role) {
        this.role = requireNonNull(role);
    }

    public void clearEstimate() {
        this.estimate = null;
    }

    public void clearReaction() {
        this.reaction = null;
    }

    public boolean hasUserId(UserId userId) {
        return this.userId.equals(userId);
    }

    public boolean hasUsername(String username) {
        return this.username.equals(username);
    }

    public boolean hasEstimate(String estimate) {
        return Objects.equals(this.estimate, estimate);
    }

    public boolean isEstimator() {
        return ESTIMATOR.equals(role);
    }

    public boolean isObserver() {
        return OBSERVER.equals(role);
    }

    public boolean hasReaction(Emoji reaction) {
        return Objects.equals(this.reaction, reaction);
    }
}
