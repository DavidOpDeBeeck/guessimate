package app.dodb.guessimate.session.drivenadapter.sessionview;

import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.api.event.EstimationRemovedEvent;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.smd.api.event.EventHandler;
import app.dodb.smd.api.event.ProcessingGroup;
import org.springframework.stereotype.Component;

import static java.util.Collections.emptySet;

@Component
@ProcessingGroup("session_view")
public class SessionViewEventHandler {

    private final SessionViewSpringRepository repository;

    public SessionViewEventHandler(SessionViewSpringRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(SessionCreatedEvent event) {
        repository.save(new SessionView(
            event.sessionId(),
            new SessionViewData(emptySet())
        ));
    }

    @EventHandler
    public void handle(EstimationAddedEvent event) {
        repository.findById(event.sessionId())
            .ifPresent(sessionView -> {
                sessionView.apply(event);
                repository.save(sessionView);
            });
    }

    @EventHandler
    public void handle(EstimationRemovedEvent event) {
        repository.findById(event.sessionId())
            .ifPresent(sessionView -> {
                sessionView.apply(event);
                repository.save(sessionView);
            });
    }
}
