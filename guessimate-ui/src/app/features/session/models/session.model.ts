import {WebSocketEventHandler, WebsocketMessage, WebSocketSchema} from '../../../websocket/services/websocket.service';

export type Session = {
  sessionId: string;
}

export type Deck = {
  deckName: string;
  cards: string[];
}

export type TimerDuration = 'DISABLED' | 'FIVE_SECONDS' | 'TEN_SECONDS' | 'THIRTY_SECONDS';

export type Emoji = 'THUMBS_UP' | 'CONFETTI' | 'HEART' | 'FIRE' | 'LIGHTNING';

export type Estimation = {
  estimationId: string;
  timestamp: string;
  estimates: string[];
  insights: EstimationInsights[];
  amountOfParticipants: number;
  votesByEstimate: { [key: string]: number };
}

export type EstimationInsights = "CONSENSUS" | "CONSENSUS_HOT_STREAK" | "SOLO_VOTE" | "HIGH_DISAGREEMENT" | "SPLIT_DECISION" | "OUTLIER_PRESENT" | "NEAR_CONSENSUS" | "FREQUENT_REVOTE" | "ALL_UNIQUE";

export type LobbyConfiguration = {
  deck: Deck;
  autoReveal: boolean;
  autoJoinRole: UserRole | null;
  timerDuration: TimerDuration;
  reactionsEnabled: boolean;
}

export type LobbyInfo = {
  sessionId: string;
  configuration: LobbyConfiguration;
  previousEstimationId: string;
  status: SessionStatus;
  users: UserInfo[];
  timerExpiresAt: string | null;
}

export type SessionStatus = "ESTIMATING" | "ESTIMATION_COMPLETED"

export type UserRole = "ESTIMATOR" | "OBSERVER"

export type UserInfo = {
  userId: string;
  username: string;
  role: UserRole | null;
  self: boolean;
  estimate: string | null;
  reaction: Emoji | null;
}

export type SetUserRoleCommand = WebsocketMessage<'SetUserRoleCommand', { role: UserRole; }>;
export type SetEstimateCommand = WebsocketMessage<'SetEstimateCommand', { estimate: string; }>;
export type ClearEstimateCommand = WebsocketMessage<'ClearEstimateCommand', {}>;
export type StartEstimationCommand = WebsocketMessage<'StartEstimationCommand', {}>;
export type CompleteEstimationCommand = WebsocketMessage<'CompleteEstimationCommand', {}>;
export type SetUsernameCommand = WebsocketMessage<'SetUsernameCommand', { username: string; }>;
export type SetDeckCommand = WebsocketMessage<'SetDeckCommand', { deckName: string; }>;
export type SetAutoRevealCommand = WebsocketMessage<'SetAutoRevealCommand', { enabled: boolean; }>;
export type SetAutoJoinCommand = WebsocketMessage<'SetAutoJoinCommand', { role: UserRole | null; }>;
export type SetTimerDurationCommand = WebsocketMessage<'SetTimerDurationCommand', { timerDuration: TimerDuration; }>;
export type SetReactionsEnabledCommand = WebsocketMessage<'SetReactionsEnabledCommand', { enabled: boolean; }>;
export type SetReactionCommand = WebsocketMessage<'SetReactionCommand', { emoji: Emoji; }>;

export type SessionWebSocketCommands = SetUserRoleCommand
  | SetEstimateCommand
  | StartEstimationCommand
  | CompleteEstimationCommand
  | SetUsernameCommand
  | SetDeckCommand
  | SetAutoRevealCommand
  | SetAutoJoinCommand
  | SetTimerDurationCommand
  | SetReactionsEnabledCommand
  | SetReactionCommand;

export type KeepAliveEvent = WebsocketMessage<'KeepAliveEvent', {}>;
export type UserConnectedEvent = WebsocketMessage<'UserConnectedEvent', { userId: string; username: string; }>;
export type UserDisconnectedEvent = WebsocketMessage<'UserDisconnectedEvent', { userId: string; }>;
export type UserRoleSetEvent = WebsocketMessage<'UserRoleSetEvent', { userId: string; role: UserRole; }>;
export type UsernameSetEvent = WebsocketMessage<'UsernameSetEvent', { userId: string; username: string; }>;
export type EstimateSetEvent = WebsocketMessage<'EstimateSetEvent', { userId: string; estimate: string; }>;
export type EstimateClearedEvent = WebsocketMessage<'EstimateClearedEvent', { userId: string; }>;
export type EstimationStartedEvent = WebsocketMessage<'EstimationStartedEvent', { timerExpiresAt: string | null }>;
export type EstimationCompletedEvent = WebsocketMessage<'EstimationCompletedEvent', { estimationId: string }>;
export type DeckSetEvent = WebsocketMessage<'DeckSetEvent', { deck: Deck }>;
export type AutoRevealEnabledEvent = WebsocketMessage<'AutoRevealEnabledEvent', { autoReveal: boolean }>;
export type AutoRevealDisabledEvent = WebsocketMessage<'AutoRevealDisabledEvent', { autoReveal: boolean }>;
export type AutoJoinUpdatedEvent = WebsocketMessage<'AutoJoinUpdatedEvent', { role: UserRole | null }>;
export type TimerDurationSetEvent = WebsocketMessage<'TimerDurationSetEvent', { timerDuration: TimerDuration }>;
export type ReactionsEnabledEvent = WebsocketMessage<'ReactionsEnabledEvent', {}>;
export type ReactionsDisabledEvent = WebsocketMessage<'ReactionsDisabledEvent', {}>;
export type ReactionSetEvent = WebsocketMessage<'ReactionSetEvent', { userId: string; emoji: Emoji }>;
export type ReactionClearedEvent = WebsocketMessage<'ReactionClearedEvent', { userId: string }>;
export type LobbyInfoSetEvent = WebsocketMessage<'LobbyInfoSetEvent', LobbyInfo>;

export type SessionWebSocketEvents = KeepAliveEvent
  | UserConnectedEvent
  | UserDisconnectedEvent
  | UserRoleSetEvent
  | UsernameSetEvent
  | EstimateSetEvent
  | EstimateClearedEvent
  | EstimationStartedEvent
  | EstimationCompletedEvent
  | DeckSetEvent
  | AutoRevealEnabledEvent
  | AutoRevealDisabledEvent
  | AutoJoinUpdatedEvent
  | TimerDurationSetEvent
  | ReactionsEnabledEvent
  | ReactionsDisabledEvent
  | ReactionSetEvent
  | ReactionClearedEvent
  | LobbyInfoSetEvent;

export type SessionWebSocketSchema = WebSocketSchema<SessionWebSocketCommands, SessionWebSocketEvents>;
export type SessionWebSocketEventHandler = WebSocketEventHandler<SessionWebSocketSchema>;
