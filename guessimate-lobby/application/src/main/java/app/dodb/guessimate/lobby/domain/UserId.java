package app.dodb.guessimate.lobby.domain;

import static java.util.Objects.requireNonNull;

public record UserId(String value) {

    public UserId {
        requireNonNull(value);
    }
}
