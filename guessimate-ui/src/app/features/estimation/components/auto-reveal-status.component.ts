import {Component, input} from '@angular/core';
import {LobbyInfo} from '../../session/models/session.model';

@Component({
  selector: 'app-auto-reveal-status',
  template: `
    @if (lobby().configuration.autoReveal) {
      <span class="flex items-center gap-1 text-xs font-medium px-1.5 py-0.5 rounded-md select-none bg-brand-100 text-brand-800 dark:bg-brand-900/40 dark:text-brand-300">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z"/>
        </svg>
        Auto Reveal
      </span>
    } @else {
      <span class="flex items-center gap-1 text-xs font-medium px-1.5 py-0.5 rounded-md select-none bg-gray-100 text-gray-500 dark:bg-gray-800 dark:text-gray-500">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12"/>
        </svg>
        Auto Reveal Off
      </span>
    }
  `
})
export class AutoRevealStatusComponent {
  lobby = input.required<LobbyInfo>();
}