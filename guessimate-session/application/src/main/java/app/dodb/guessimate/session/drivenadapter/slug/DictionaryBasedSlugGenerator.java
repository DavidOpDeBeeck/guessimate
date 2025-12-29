package app.dodb.guessimate.session.drivenadapter.slug;

import app.dodb.guessimate.session.port.SlugGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static app.dodb.guessimate.session.utils.FileUtils.readLines;

@Component
public class DictionaryBasedSlugGenerator implements SlugGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryBasedSlugGenerator.class);
    private static final Random RANDOM = new SecureRandom();

    private final List<String> adjectives;
    private final List<String> nouns;

    public DictionaryBasedSlugGenerator(@Value("classpath:adjectives.txt") Resource adjectivesResource,
                                        @Value("classpath:nouns.txt") Resource nounsResource) throws IOException {
        this.adjectives = readLines(adjectivesResource);
        this.nouns = readLines(nounsResource);
        LOGGER.info("Initialized SlugGenerator with {} adjectives and {} nouns", adjectives.size(), nouns.size());
    }

    @Override
    public String generateSlug() {
        return "%s-%s-%s".formatted(
            adjectives.get(RANDOM.nextInt(adjectives.size())),
            nouns.get(RANDOM.nextInt(nouns.size())),
            nouns.get(RANDOM.nextInt(nouns.size()))
        );
    }

}
