import {Component, computed, input, output} from '@angular/core';
import {LobbyInfo} from '../../session/models/session.model';

@Component({
  selector: 'app-reactions-enabled-selector',
  standalone: true,
  template: `
    <div class="flex items-center justify-between py-3 px-4">
      <div class="flex gap-3 items-center">
        <div class="p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-900 dark:text-white">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M15.182 15.182a4.5 4.5 0 0 1-6.364 0M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0ZM9.75 9.75c0 .414-.168.75-.375.75S9 10.164 9 9.75 9.168 9 9.375 9s.375.336.375.75Zm-.375 0h.008v.015h-.008V9.75Zm5.625 0c0 .414-.168.75-.375.75s-.375-.336-.375-.75.168-.75.375-.75.375.336.375.75Zm-.375 0h.008v.015h-.008V9.75Z"/>
          </svg>
        </div>
        <div class="flex flex-col">
          <span class="text-sm font-medium text-gray-900 dark:text-white">Reactions</span>
          <span class="text-xs text-gray-500 dark:text-gray-400">Allow emoji reactions</span>
        </div>
      </div>
      <div class="inline-flex rounded-md bg-surface-200 dark:bg-gray-800 p-0.5 shrink-0">
        <button type="button"
                (click)="setReactionsEnabled.emit(false)"
                [class]="!reactionsEnabled()
                    ? 'px-2 py-1 text-xs font-medium rounded bg-surface-50 dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Disabled
        </button>
        <button type="button"
                (click)="setReactionsEnabled.emit(true)"
                [class]="reactionsEnabled()
                    ? 'px-2 py-1 text-xs font-medium rounded bg-brand-600 text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Enabled
        </button>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: contents
    }
  `]
})
export class ReactionsEnabledSelectorComponent {

  lobby = input.required<LobbyInfo>();

  setReactionsEnabled = output<boolean>();

  reactionsEnabled = computed(() => this.lobby().configuration.reactionsEnabled);
}

