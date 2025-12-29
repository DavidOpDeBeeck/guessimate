import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {NgClass} from '@angular/common';
import {WebSocketConnection} from '../services/websocket.service';

@Component({
  selector: 'app-web-socket-overlay',
  imports: [
    ReactiveFormsModule,
    NgClass
  ],
  template: `
    <div class="relative">
      <div class="transition-all duration-500 ease-in-out absolute inset-0 bg-gray-100/10 dark:bg-gray-950/60 rounded-md backdrop-blur-xs z-2" aria-hidden="true" [ngClass]="overlayClasses()">
        <div class="h-full flex flex-col gap-4 justify-center items-center">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="text-gray-900 dark:text-white size-8">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="m3 3 8.735 8.735m0 0a.374.374 0 1 1 .53.53m-.53-.53.53.53m0 0L21 21M14.652 9.348a3.75 3.75 0 0 1 0 5.304m2.121-7.425a6.75 6.75 0 0 1 0 9.546m2.121-11.667c3.808 3.807 3.808 9.98 0 13.788m-9.546-4.242a3.733 3.733 0 0 1-1.06-2.122m-1.061 4.243a6.75 6.75 0 0 1-1.625-6.929m-.496 9.05c-3.068-3.067-3.664-7.67-1.79-11.334M12 12h.008v.008H12V12Z"/>
          </svg>
          <span class="text-xl font-semibold leading-none text-gray-900 dark:text-white">You’ve lost connection.</span>
          <button (click)="reconnect()"
                  class="flex items-center justify-center gap-2 py-2 px-3 text-sm font-medium text-gray-900 bg-gray-50/50 rounded-full border border-gray-100 hover:bg-gray-50 hover:text-brand-700 focus-visible:outline-none focus-visible:ring-4 focus-visible:ring-brand-200 focus:z-10 dark:focus-visible:ring-gray-800 dark:bg-gray-900/40 dark:text-gray-400 dark:border-gray-800/60 dark:hover:text-white dark:hover:bg-gray-800/60 cursor-pointer">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5"
                 [ngClass]="{'animate-spin': reconnecting()}">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"/>
            </svg>
            <span class="font-semibold leading-none">{{ reconnecting() ? "Reconnecting" : "Reconnect" }}</span>
          </button>
        </div>
      </div>
      <ng-content></ng-content>
    </div>
  `
})
export class WebSocketOverlayComponent {

  connection = input.required<WebSocketConnection<any> | undefined>();

  disconnected = computed(() => this.connection()?.state() == 'DISCONNECTED');
  reconnecting = computed(() => this.connection()?.state() == 'RECONNECTING');

  overlayClasses = computed(() => {
    if (this.connection()) {
      return this.disconnected() || this.reconnecting()
        ? 'opacity-100 pointer-events-auto'
        : 'opacity-0 pointer-events-none';
    }
    return "";
  });

  reconnect() {
    this.connection()?.reconnect();
  }
}

