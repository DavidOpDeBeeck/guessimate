package app.dodb.guessimate.session.usecase;

import app.dodb.guessimate.session.api.command.AddEstimationCommand;
import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.EstimationId;
import app.dodb.guessimate.session.domain.Session;
import app.dodb.guessimate.session.domain.SessionId;
import app.dodb.guessimate.session.domain.deck.Deck;
import app.dodb.guessimate.session.port.SessionRepository;
import app.dodb.smd.api.command.CommandHandler;
import app.dodb.smd.api.event.EventPublisher;
import app.dodb.smd.api.metadata.datetime.DatetimeProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddEstimationCommandHandler {

    private final EventPublisher eventPublisher;
    private final SessionRepository sessionRepository;
    private final DatetimeProvider datetimeProvider;

    public AddEstimationCommandHandler(EventPublisher eventPublisher,
                                       SessionRepository sessionRepository,
                                       DatetimeProvider datetimeProvider) {
        this.eventPublisher = eventPublisher;
        this.sessionRepository = sessionRepository;
        this.datetimeProvider = datetimeProvider;
    }

    @CommandHandler
    public String handle(AddEstimationCommand command) {
        SessionId sessionId = new SessionId(command.sessionId());
        Session session = sessionRepository.findBy(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("No session found with id: " + sessionId.value()));

        EstimationId estimationId = session.addEstimation(Deck.from(command.deck()), extractEstimates(command), datetimeProvider.now());
        session.consumeEvents().forEach(eventPublisher::publish);

        return estimationId.value();
    }

    private static List<Estimate> extractEstimates(AddEstimationCommand command) {
        return command.estimates().stream()
            .map(Estimate::new)
            .toList();
    }
}
