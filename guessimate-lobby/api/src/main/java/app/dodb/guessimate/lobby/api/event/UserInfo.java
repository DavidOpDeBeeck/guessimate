package app.dodb.guessimate.lobby.api.event;

public record UserInfo(String userId, String username, String estimate, Emoji reaction, UserRole role, boolean self) {
}
