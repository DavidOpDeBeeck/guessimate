import {Component, computed, input, output} from '@angular/core';
import {LobbyInfo, TimerDuration} from '../../session/models/session.model';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-timer-duration-selector',
  imports: [NgClass],
  standalone: true,
  template: `
    <div class="flex items-center justify-between py-3 px-4">
      <div class="flex gap-3 items-center">
        <div class="p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-900 dark:text-white">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
          </svg>
        </div>
        <div class="flex flex-col">
          <span class="text-sm font-medium text-gray-900 dark:text-white">Timer</span>
          <span class="text-xs text-gray-500 dark:text-gray-400">Reveal when time expires</span>
        </div>
      </div>
      <div class="inline-flex rounded-md bg-surface-200 dark:bg-gray-800 p-0.5 shrink-0">
        <button type="button"
                (click)="setTimerDuration.emit('DISABLED')"
                [ngClass]="buttonClasses('DISABLED')"
                class="px-2 py-1 text-xs font-medium rounded cursor-pointer">
          Off
        </button>
        <button type="button"
                (click)="setTimerDuration.emit('FIVE_SECONDS')"
                [ngClass]="buttonClasses('FIVE_SECONDS')"
                class="px-2 py-1 text-xs font-medium rounded cursor-pointer">
          5s
        </button>
        <button type="button"
                (click)="setTimerDuration.emit('TEN_SECONDS')"
                [ngClass]="buttonClasses('TEN_SECONDS')"
                class="px-2 py-1 text-xs font-medium rounded cursor-pointer">
          10s
        </button>
        <button type="button"
                (click)="setTimerDuration.emit('THIRTY_SECONDS')"
                [ngClass]="buttonClasses('THIRTY_SECONDS')"
                class="px-2 py-1 text-xs font-medium rounded cursor-pointer">
          30s
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
export class TimerDurationSelectorComponent {

  lobby = input.required<LobbyInfo>();
  setTimerDuration = output<TimerDuration>();

  currentDuration = computed(() => this.lobby().configuration.timerDuration);

  buttonClasses(duration: TimerDuration): string {
    const isActive = this.currentDuration() === duration;

    if (duration === 'DISABLED') {
      return isActive
        ? 'bg-surface-50 dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm'
        : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300';
    }

    return isActive
      ? 'bg-brand-600 text-white shadow-sm'
      : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300';
  }
}

