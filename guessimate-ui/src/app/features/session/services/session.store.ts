import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {forkJoin} from 'rxjs';
import {
  AutoJoinUpdatedEvent,
  ClearEstimateCommand,
  CompleteEstimationCommand,
  DeckSetEvent,
  Emoji,
  EstimateClearedEvent,
  EstimateSetEvent,
  Estimation,
  EstimationCompletedEvent,
  LobbyInfo,
  LobbyInfoSetEvent,
  ReactionClearedEvent,
  ReactionSetEvent,
  Session,
  SessionWebSocketSchema,
  SetAutoJoinCommand,
  SetAutoRevealCommand,
  SetDeckCommand,
  SetEstimateCommand,
  SetReactionCommand,
  SetReactionsEnabledCommand,
  SetTimerDurationCommand,
  SetUsernameCommand,
  SetUserRoleCommand,
  StartEstimationCommand,
  TimerDuration,
  TimerDurationSetEvent,
  UserConnectedEvent,
  UserDisconnectedEvent,
  UsernameSetEvent,
  UserRole,
  UserRoleSetEvent
} from '../models/session.model';
import {WebSocketConnection} from '../../../websocket/services/websocket.service';
import {environment} from '../../../../environments/environment';
import {DebugService} from '../../debug/services/debug.service';

@Injectable()
export class SessionStore {

  private readonly http = inject(HttpClient);
  private readonly debugService = inject(DebugService);

  readonly lobby = signal<LobbyInfo | null>(null);
  readonly estimations = signal<Estimation[]>([]);

  connection = signal<WebSocketConnection<SessionWebSocketSchema> | undefined>(undefined);

  initialize(sessionId: string): void {
    forkJoin([
      this.http.get<Session>(`/api/sessions/${sessionId}`),
      this.http.get<Estimation[]>(`/api/sessions/${sessionId}/estimations`)
    ]).subscribe({
      next: ([session, estimations]) => {
        this.estimations.set(estimations);

        const username = localStorage.getItem("username");
        const url = username
          ? `${environment.webSocketBaseUrl}/ws/lobby/${session.sessionId}?username=${username}`
          : `${environment.webSocketBaseUrl}/ws/lobby/${session.sessionId}`;

        const connection = new WebSocketConnection<SessionWebSocketSchema>(url, [
          {
            EstimationCompletedEvent: () => {
              this.http.get<Estimation[]>(`/api/sessions/${this.lobby()?.sessionId}/estimations`)
                .subscribe((estimations) => this.estimations.set(estimations));
            }
          },
          {
            UserConnectedEvent: (event: UserConnectedEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: [...lobbyInfo.users, {
                  userId: event.userId,
                  username: event.username,
                  role: null,
                  self: false,
                  estimate: null,
                  reaction: null
                }]
              }));
            },
            UserDisconnectedEvent: (event: UserDisconnectedEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: lobbyInfo.users.filter(user => user.userId !== event.userId)
              }));
            },
            UserRoleSetEvent: (event: UserRoleSetEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: lobbyInfo.users.map(user =>
                  user.userId === event.userId ? {...user, role: event.role} : user
                )
              }));
            },
            UsernameSetEvent: (event: UsernameSetEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: lobbyInfo.users.map(user =>
                  user.userId === event.userId ? {...user, username: event.username} : user
                )
              }));
            },
            EstimateSetEvent: (event: EstimateSetEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: lobbyInfo.users.map(user =>
                  user.userId === event.userId ? {...user, estimate: event.estimate} : user
                )
              }));
            },
            EstimateClearedEvent: (event: EstimateClearedEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: lobbyInfo.users.map(user =>
                  user.userId === event.userId ? {...user, estimate: null} : user
                )
              }));
            },
            EstimationStartedEvent: (event) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                status: "ESTIMATING",
                timerExpiresAt: event.timerExpiresAt
              }));
            },
            EstimationCompletedEvent: (event: EstimationCompletedEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                status: "ESTIMATION_COMPLETED",
                previousEstimationId: event.estimationId
              }));
            },
            DeckSetEvent: (event: DeckSetEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                configuration: {
                  ...lobbyInfo.configuration,
                  deck: event.deck
                }
              }));
            },
            AutoRevealEnabledEvent: () => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                configuration: {
                  ...lobbyInfo.configuration,
                  autoReveal: true
                }
              }));
            },
            AutoRevealDisabledEvent: () => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                configuration: {
                  ...lobbyInfo.configuration,
                  autoReveal: false
                }
              }));
            },
            AutoJoinUpdatedEvent: (event: AutoJoinUpdatedEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                configuration: {
                  ...lobbyInfo.configuration,
                  autoJoinRole: event.role
                }
              }));
            },
            TimerDurationSetEvent: (event: TimerDurationSetEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                configuration: {
                  ...lobbyInfo.configuration,
                  timerDuration: event.timerDuration
                }
              }));
            },
            ReactionsEnabledEvent: () => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                configuration: {
                  ...lobbyInfo.configuration,
                  reactionsEnabled: true
                }
              }));
            },
            ReactionsDisabledEvent: () => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                configuration: {
                  ...lobbyInfo.configuration,
                  reactionsEnabled: false
                }
              }));
            },
            ReactionSetEvent: (event: ReactionSetEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: lobbyInfo.users.map(user =>
                  user.userId === event.userId ? {...user, reaction: event.emoji} : user
                )
              }));
            },
            ReactionClearedEvent: (event: ReactionClearedEvent) => {
              this.update(lobbyInfo => ({
                ...lobbyInfo,
                users: lobbyInfo.users.map(user =>
                  user.userId === event.userId ? {...user, reaction: null} : user
                )
              }));
            },
            LobbyInfoSetEvent: (event: LobbyInfoSetEvent) => {
              this.lobby.set(event);
            }
          }
        ], {
          onSend: (msg) => this.debugService.logSent(sessionId, msg),
          onReceive: (msg) => this.debugService.logReceived(sessionId, msg)
        });

        this.connection.set(connection);
        connection.connect();
      }
    });
  }

  destroy(): void {
    this.connection()?.disconnect();
  }

  reconnect(): void {
    this.connection()?.reconnect();
  }

  getConnection(): WebSocketConnection<SessionWebSocketSchema> | undefined {
    return this.connection();
  }

  // Commands
  startEstimation(): void {
    const command: StartEstimationCommand = {
      type: "StartEstimationCommand"
    };
    this.connection()?.send(command);
  }

  revealEstimation(): void {
    const command: CompleteEstimationCommand = {
      type: "CompleteEstimationCommand"
    };
    this.connection()?.send(command);
  }

  setEstimate(estimate: string): void {
    const command: SetEstimateCommand = {
      type: "SetEstimateCommand",
      estimate: estimate
    };
    this.connection()?.send(command);
  }

  clearEstimate(): void {
    const command: ClearEstimateCommand = {
      type: "ClearEstimateCommand"
    };
    this.connection()?.send(command);
  }

  sendReaction(reaction: Emoji): void {
    const command: SetReactionCommand = {
      type: "SetReactionCommand",
      emoji: reaction
    };
    this.connection()?.send(command);
  }

  setUserRole(role: UserRole): void {
    const command: SetUserRoleCommand = {
      type: "SetUserRoleCommand",
      role: role
    };
    this.connection()?.send(command);
  }

  setUsername(username: string): void {
    const command: SetUsernameCommand = {
      type: "SetUsernameCommand",
      username: username
    };
    this.connection()?.send(command);
  }

  setDeck(name: string): void {
    const command: SetDeckCommand = {
      type: "SetDeckCommand",
      name
    };
    this.connection()?.send(command);
  }

  setAutoReveal(enabled: boolean): void {
    const command: SetAutoRevealCommand = {
      type: "SetAutoRevealCommand",
      enabled: enabled
    };
    this.connection()?.send(command);
  }

  setAutoJoin(role: UserRole | null): void {
    const command: SetAutoJoinCommand = {
      type: "SetAutoJoinCommand",
      role: role
    };
    this.connection()?.send(command);
  }

  setTimerDuration(timerDuration: TimerDuration): void {
    const command: SetTimerDurationCommand = {
      type: "SetTimerDurationCommand",
      timerDuration: timerDuration
    };
    this.connection()?.send(command);
  }

  setReactionsEnabled(enabled: boolean): void {
    const command: SetReactionsEnabledCommand = {
      type: "SetReactionsEnabledCommand",
      enabled: enabled
    };
    this.connection()?.send(command);
  }

  private update(updateFn: (current: LobbyInfo) => LobbyInfo): void {
    this.lobby.update(lobbyInfo => {
      if (lobbyInfo == null) {
        return null;
      }
      return updateFn(lobbyInfo);
    });
  }
}
