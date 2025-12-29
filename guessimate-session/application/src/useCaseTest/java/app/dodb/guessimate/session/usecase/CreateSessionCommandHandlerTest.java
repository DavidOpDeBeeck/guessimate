package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.api.command.CreateSessionCommand;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.guessimate.session.usecase.stubs.SlugGeneratorStub;
import app.dodb.smd.test.SMDTestExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UseCaseTest
class CreateSessionCommandHandlerTest {

    private static final String SLUG = "slug-for-testing";

    @Inject
    private SMDTestExtension smd;
    @Inject
    private SlugGeneratorStub slugGenerator;

    @Test
    void createSession() {
        slugGenerator.stubSlug(SLUG);

        String sessionId = smd.send(new CreateSessionCommand());

        assertThat(sessionId).isEqualTo(SLUG);
        assertThat(smd.getEvents())
            .containsExactly(new SessionCreatedEvent(SLUG));
    }
}