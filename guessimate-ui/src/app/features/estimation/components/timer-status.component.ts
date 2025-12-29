import {Component, computed, input} from '@angular/core';
import {LobbyInfo} from '../../session/models/session.model';

@Component({
  selector: 'app-timer-status',
  template: `
    @if (timerDuration()) {
      <span class="flex items-center gap-1 text-xs font-medium px-1.5 py-0.5 rounded-md select-none bg-brand-100 text-brand-800 dark:bg-brand-900/40 dark:text-brand-300">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
        </svg>
        {{ timerDuration() }} Timer
      </span>
    } @else {
      <span class="flex items-center gap-1 text-xs font-medium px-1.5 py-0.5 rounded-md select-none bg-gray-100 text-gray-500 dark:bg-gray-800 dark:text-gray-500">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
        </svg>
        Timer Off
      </span>
    }
  `
})
export class TimerStatusComponent {

  lobby = input.required<LobbyInfo>();

  timerDuration = computed(() => {
    switch (this.lobby().configuration.timerDuration) {
      case 'FIVE_SECONDS':
        return '5s';
      case 'TEN_SECONDS':
        return '10s';
      case 'THIRTY_SECONDS':
        return '30s';
      default:
        return undefined;
    }
  });
}