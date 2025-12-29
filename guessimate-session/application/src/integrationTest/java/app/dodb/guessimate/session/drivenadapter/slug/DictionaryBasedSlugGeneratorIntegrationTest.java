package app.dodb.guessimate.session.drivenadapter.slug;

import app.dodb.guessimate.session.IntegrationTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class DictionaryBasedSlugGeneratorIntegrationTest {

    @Inject
    DictionaryBasedSlugGenerator slugGenerator;

    @Test
    void generateSlug_usesCorrectFormat() {
        String actual = slugGenerator.generateSlug();

        assertThat(actual).matches("^([A-Za-z]+-){2,}[A-Za-z]+$");
    }
}
