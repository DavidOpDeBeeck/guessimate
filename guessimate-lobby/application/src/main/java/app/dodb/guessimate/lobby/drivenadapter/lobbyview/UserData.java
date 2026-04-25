package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.api.event.Emoji;
import app.dodb.guessimate.lobby.api.event.UserRole;

import java.util.Optional;

public record UserData(
    String userId,
    String username,
    String estimate,
    Emoji reaction,
    UserRole role
) {

    public UserData(String userId, String username) {
        this(userId, username, null, null, null);
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

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public UserData withUsername(String username) {
        return new UserData(userId, username, estimate, reaction, role);
    }

    public UserData withEstimate(String estimate) {
        return new UserData(userId, username, estimate, reaction, role);
    }

    public UserData withReaction(Emoji reaction) {
        return new UserData(userId, username, estimate, reaction, role);
    }

    public UserData withRole(UserRole role) {
        return new UserData(userId, username, estimate, reaction, role);
    }
}