package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.api.command.CreateSessionCommand;
import app.dodb.guessimate.session.domain.Session;
import app.dodb.guessimate.session.domain.SessionId;
import app.dodb.guessimate.session.port.SlugGenerator;
import app.dodb.smd.api.command.CommandHandler;
import app.dodb.smd.api.event.EventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CreateSessionCommandHandler {

    private final EventPublisher eventPublisher;
    private final SlugGenerator slugGenerator;

    public CreateSessionCommandHandler(EventPublisher eventPublisher,
                                       SlugGenerator slugGenerator) {
        this.eventPublisher = eventPublisher;
        this.slugGenerator = slugGenerator;
    }

    @CommandHandler
    public String handle(CreateSessionCommand command) {
        SessionId sessionId = new SessionId(slugGenerator.generateSlug());

        Session session = new Session(sessionId);
        session.consumeEvents().forEach(eventPublisher::publish);

        return sessionId.value();
    }
}
