package app.dodb.guessimate.session.api.event;

import app.dodb.smd.api.event.Event;

public sealed interface SessionEvent extends Event
    permits SessionCreatedEvent,
    EstimationAddedEvent,
    EstimationRemovedEvent {

}
