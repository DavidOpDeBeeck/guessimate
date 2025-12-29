package app.dodb.guessimate.session.domain;

import static java.util.Objects.requireNonNull;

public record SessionId(String value) {

    public SessionId {
        requireNonNull(value);
    }
}
