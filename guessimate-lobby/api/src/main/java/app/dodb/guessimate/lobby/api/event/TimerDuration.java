package app.dodb.guessimate.lobby.api.event;

import java.time.Instant;
import java.util.Optional;

public enum TimerDuration {

    DISABLED {
        @Override
        public Optional<Instant> calculateExpiryTime(Instant now) {
            return Optional.empty();
        }
    },
    FIVE_SECONDS {
        @Override
        public Optional<Instant> calculateExpiryTime(Instant now) {
            return Optional.of(now.plusSeconds(5));
        }
    },
    TEN_SECONDS {
        @Override
        public Optional<Instant> calculateExpiryTime(Instant now) {
            return Optional.of(now.plusSeconds(10));
        }
    },
    THIRTY_SECONDS {
        @Override
        public Optional<Instant> calculateExpiryTime(Instant now) {
            return Optional.of(now.plusSeconds(30));
        }
    };

    public abstract Optional<Instant> calculateExpiryTime(Instant now);
}

