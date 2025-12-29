package app.dodb.guessimate.lobby.api.event;

import app.dodb.smd.api.event.Event;

public sealed interface LobbyEvent extends Event
    permits AutoJoinUpdatedEvent,
    AutoRevealDisabledEvent,
    AutoRevealEnabledEvent,
    DeckSetEvent,
    EstimateClearedEvent,
    EstimateSetEvent,
    EstimationCompletedEvent,
    EstimationStartedEvent,
    ReactionClearedEvent,
    ReactionsDisabledEvent,
    ReactionsEnabledEvent,
    ReactionSetEvent,
    TimerDurationSetEvent,
    UserConnectedEvent,
    UserDisconnectedEvent,
    UserRoleSetEvent,
    UsernameSetEvent,
    KeepAliveEvent {

    String sessionId();
}
