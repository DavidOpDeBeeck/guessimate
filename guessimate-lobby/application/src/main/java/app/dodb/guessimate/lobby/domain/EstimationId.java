package app.dodb.guessimate.lobby.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record EstimationId(String value) {

    public static EstimationId generate() {
        return new EstimationId(UUID.randomUUID().toString());
    }

    public EstimationId {
        requireNonNull(value);
    }
}
