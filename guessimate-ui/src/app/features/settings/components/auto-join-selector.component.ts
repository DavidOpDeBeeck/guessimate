import {Component, computed, input, output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {LobbyInfo, UserRole} from '../../session/models/session.model';

@Component({
  selector: 'app-auto-join-selector',
  imports: [FormsModule],
  standalone: true,
  template: `
    <div class="flex items-center justify-between py-3 px-4">
      <div class="flex gap-3 items-center">
        <div class="p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-900 dark:text-white">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
          </svg>
        </div>
        <div class="flex flex-col">
          <span class="text-sm font-medium text-gray-900 dark:text-white">Auto Join</span>
          <span class="text-xs text-gray-500 dark:text-gray-400">Assign role automatically on join</span>
        </div>
      </div>
      <div class="inline-flex rounded-md bg-surface-200 dark:bg-gray-800 p-0.5 shrink-0">
        <button type="button"
                (click)="setAutoJoin.emit(null)"
                [class]="autoJoin() === null
                    ? 'px-2 py-1 text-xs font-medium rounded bg-surface-50 dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Disabled
        </button>
        <button type="button"
                (click)="setAutoJoin.emit('ESTIMATOR')"
                [class]="autoJoin() === 'ESTIMATOR'
                    ? 'px-2 py-1 text-xs font-medium rounded bg-brand-600 text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Estimator
        </button>
        <button type="button"
                (click)="setAutoJoin.emit('OBSERVER')"
                [class]="autoJoin() === 'OBSERVER'
                    ? 'px-2 py-1 text-xs font-medium rounded bg-brand-600 text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Observer
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
export class AutoJoinSelectorComponent {

  lobby = input.required<LobbyInfo>();

  setAutoJoin = output<UserRole | null>();

  autoJoin = computed(() => this.lobby().configuration.autoJoinRole);
}
