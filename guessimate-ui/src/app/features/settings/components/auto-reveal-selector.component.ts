import {Component, computed, input, output} from '@angular/core';
import {LobbyInfo} from '../../session/models/session.model';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-auto-reveal-selector',
  imports: [FormsModule],
  standalone: true,
  template: `
    <div class="flex items-center justify-between py-3 px-4">
      <div class="flex gap-3 items-center">
        <div class="p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-900 dark:text-white">
            <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z"/>
          </svg>
        </div>
        <div class="flex flex-col">
          <span class="text-sm font-medium text-gray-900 dark:text-white">Auto Reveal</span>
          <span class="text-xs text-gray-500 dark:text-gray-400">Reveal when all have voted</span>
        </div>
      </div>
      <div class="inline-flex rounded-md bg-surface-200 dark:bg-gray-800 p-0.5 shrink-0">
        <button type="button"
                (click)="setAutoReveal.emit(false)"
                [class]="!autoRevealEnabled()
                    ? 'px-2 py-1 text-xs font-medium rounded bg-surface-50 dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Disabled
        </button>
        <button type="button"
                (click)="setAutoReveal.emit(true)"
                [class]="autoRevealEnabled()
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
export class AutoRevealSelectorComponent {

  lobby = input.required<LobbyInfo>();

  setAutoReveal = output<boolean>();

  autoRevealEnabled = computed(() => this.lobby().configuration.autoReveal);
}

