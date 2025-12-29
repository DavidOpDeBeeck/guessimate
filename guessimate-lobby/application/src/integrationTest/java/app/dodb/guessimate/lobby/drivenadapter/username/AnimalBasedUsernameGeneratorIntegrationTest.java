package app.dodb.guessimate.lobby.drivenadapter.username;

import app.dodb.guessimate.lobby.IntegrationTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class AnimalBasedUsernameGeneratorIntegrationTest {

    @Inject
    AnimalBasedUsernameGenerator usernameGenerator;

    @Test
    void generate_returnsNonEmptyUsername() {
        String username = usernameGenerator.generate();

        assertThat(username).isNotBlank();
    }

    @Test
    void generate_canProduceDifferentUsernames() {
        var usernames = range(0, 50)
            .mapToObj(i -> usernameGenerator.generate())
            .distinct()
            .toList();

        assertThat(usernames).hasSizeGreaterThan(5);
    }
}
