package app.dodb.guessimate.session.api;

import static java.util.Objects.requireNonNull;

public record SessionTO(String sessionId) {

    public SessionTO {
        requireNonNull(sessionId);
    }
}
