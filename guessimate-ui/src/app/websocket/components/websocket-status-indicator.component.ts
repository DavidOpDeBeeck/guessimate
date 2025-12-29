import {Component, input} from '@angular/core';
import {WebSocketConnection} from '../services/websocket.service';

@Component({
  selector: 'app-web-socket-status-indicator',
  imports: [],
  template: `
    @if (connection()?.state() === "INITIAL" || connection()?.state() === "CONNECTING" || connection()?.state() === "RECONNECTING") {
      <span
        class="inline-flex gap-2 items-center bg-brand-100 text-brand-600 text-xs font-medium px-2.5 py-0.5 rounded-full animate-pulse dark:bg-brand-900/30 dark:text-brand-400">
          Connecting
      </span>
    }
    @if (connection()?.state() === "CONNECTED") {
      <span
        class="inline-flex gap-2 items-center bg-success-100 text-success-600 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-success-900/30 dark:text-success-400">
          <span class="w-2 h-2 bg-success-600 rounded-full"></span>
          Connected
      </span>
    }
    @if (connection()?.state() === "DISCONNECTED") {
      <span
        class="inline-flex gap-2 items-center bg-danger-100 text-danger-600 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-danger-900/30 dark:text-danger-400">
          <span class="w-2 h-2 bg-danger-600 rounded-full"></span>
          Disconnected
      </span>
    }
  `
})
export class WebSocketStatusIndicatorComponent {

  connection = input.required<WebSocketConnection<any> | undefined>();

}