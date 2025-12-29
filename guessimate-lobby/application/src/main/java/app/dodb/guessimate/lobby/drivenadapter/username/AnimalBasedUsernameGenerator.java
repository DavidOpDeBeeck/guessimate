package app.dodb.guessimate.lobby.drivenadapter.username;

import app.dodb.guessimate.lobby.port.UsernameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static app.dodb.guessimate.lobby.utils.FileUtils.readLines;

@Component
public class AnimalBasedUsernameGenerator implements UsernameGenerator {

    private static final Random RANDOM = new SecureRandom();

    private final List<String> animals;

    public AnimalBasedUsernameGenerator(@Value("classpath:animals.txt") Resource animalsResource) throws IOException {
        this.animals = readLines(animalsResource);
    }

    @Override
    public String generate() {
        return animals.get(RANDOM.nextInt(animals.size()));
    }
}
