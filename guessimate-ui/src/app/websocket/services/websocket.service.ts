import {signal} from '@angular/core';

export type WebSocketState = "INITIAL" | "CONNECTING" | "RECONNECTING" | "CONNECTED" | "DISCONNECTED";

export type WebsocketMessage<T extends string, P> = { type: T } & P;
export type WebSocketSchema<C extends WebsocketMessage<any, any>, E extends WebsocketMessage<any, any>> = {
  commands: C;
  events: E;
};
export type WebSocketEventHandler<S extends WebSocketSchema<any, any>> = {
  [K in S["events"]["type"]]: (event: Extract<S["events"], { type: K }>) => void;
};

export class WebSocketConnection<S extends WebSocketSchema<any, any>> {

  public state = signal<WebSocketState>("INITIAL");
  private socket: WebSocket | undefined;

  constructor(
    private readonly url: string,
    private readonly handlers: Partial<WebSocketEventHandler<S>>[],
    private readonly debugCallbacks?: {
      onSend?: (message: any) => void;
      onReceive?: (message: any) => void;
    }
  ) {
  }

  connect(reconnect: boolean = false) {
    const socket = new WebSocket(this.url);
    this.socket = socket;

    this.state.set(reconnect ? "RECONNECTING" : "CONNECTING");
    socket.onopen = () => this.state.set("CONNECTED");
    socket.onerror = () => this.state.set("DISCONNECTED");
    socket.onclose = () => this.state.set("DISCONNECTED");
    socket.onmessage = (event) => {
      const eventData = JSON.parse(event.data) as any;
      this.debugCallbacks?.onReceive?.(eventData);
      for (const handler of this.handlers) {
        handler[eventData.type as keyof WebSocketEventHandler<S>]?.call(this, eventData as any);
      }
    };
  }

  reconnect() {
    this.disconnect();
    this.connect(true);
  }

  disconnect() {
    if (this.state() !== "CONNECTING") {
      this.socket?.close();
    }
  }

  send(message: WebsocketMessage<any, any>) {
    if (this.state() === "CONNECTED") {
      this.debugCallbacks?.onSend?.(message);
      this.socket?.send(JSON.stringify(message));
    }
  }
}
