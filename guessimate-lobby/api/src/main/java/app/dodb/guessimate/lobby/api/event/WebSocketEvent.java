package app.dodb.guessimate.lobby.api.event;

import app.dodb.smd.api.event.Event;

public sealed interface WebSocketEvent extends Event
    permits EstimateSetEvent,
    EstimateClearedEvent,
    EstimationCompletedEvent,
    EstimationStartedEvent,
    KeepAliveEvent,
    LobbyInfoSetEvent,
    UserConnectedEvent,
    UserDisconnectedEvent,
    UsernameSetEvent,
    UserRoleSetEvent,
    DeckSetEvent,
    AutoRevealEnabledEvent,
    AutoRevealDisabledEvent,
    TimerDurationSetEvent,
    ReactionsDisabledEvent,
    ReactionsEnabledEvent,
    ReactionSetEvent,
    ReactionClearedEvent,
    AutoJoinUpdatedEvent {

    String sessionId();
}
