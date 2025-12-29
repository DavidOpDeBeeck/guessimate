import {Component, inject} from '@angular/core';
import {DeckSelectorComponent} from '../components/deck-selector.component';
import {UsernameSelectorComponent} from '../components/username-selector.component';
import {AutoRevealSelectorComponent} from '../components/auto-reveal-selector.component';
import {TimerDurationSelectorComponent} from '../components/timer-duration-selector.component';
import {ReactionsEnabledSelectorComponent} from '../components/reactions-enabled-selector.component';
import {AutoJoinSelectorComponent} from '../components/auto-join-selector.component';
import {DebugModeSelectorComponent} from '../components/debug-mode-selector.component';
import {AppearanceSelectorComponent} from '../components/appearance-selector.component';
import {SessionStore} from '../../session/services/session.store';

@Component({
  selector: 'app-settings-page',
  imports: [
    DeckSelectorComponent,
    UsernameSelectorComponent,
    AutoRevealSelectorComponent,
    TimerDurationSelectorComponent,
    ReactionsEnabledSelectorComponent,
    AutoJoinSelectorComponent,
    DebugModeSelectorComponent,
    AppearanceSelectorComponent
  ],
  standalone: true,
  template: `
    <div class="grid grid-cols-1 gap-4">
      <div class="flex flex-col bg-surface-100/60 border border-surface-300 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
        <div class="flex flex-col gap-1 py-3 px-4">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">Personal Settings</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Manage your personal preferences.</span>
        </div>
        <app-username-selector [lobby]="store.lobby()!" (setUsername)="store.setUsername($event)"/>
        <app-appearance-selector/>
        <app-debug-mode-selector/>
      </div>
      <div class="flex flex-col bg-surface-100/60 border border-surface-300 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
        <div class="flex flex-col gap-1 py-3 px-4">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 0 1 1.37.49l1.296 2.247a1.125 1.125 0 0 1-.26 1.431l-1.003.827c-.293.24-.438.613-.431.992a6.759 6.759 0 0 1 0 .255c-.007.378.138.75.43.99l1.005.828c.424.35.534.954.26 1.43l-1.298 2.247a1.125 1.125 0 0 1-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 0 1-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 0 1-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 0 1-1.369-.49l-1.297-2.247a1.125 1.125 0 0 1 .26-1.431l1.004-.827c.292-.24.437-.613.43-.992a6.932 6.932 0 0 1 0-.255c.007-.378-.138-.75-.43-.99l-1.004-.828a1.125 1.125 0 0 1-.26-1.43l1.297-2.247a1.125 1.125 0 0 1 1.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.281Z"/>
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">Session Settings</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Manage session configuration.</span>
        </div>
        <app-auto-join-selector [lobby]="store.lobby()!" (setAutoJoin)="store.setAutoJoin($event)"/>
        <app-auto-reveal-selector [lobby]="store.lobby()!" (setAutoReveal)="store.setAutoReveal($event)"/>
        <app-timer-duration-selector [lobby]="store.lobby()!" (setTimerDuration)="store.setTimerDuration($event)"/>
        <app-reactions-enabled-selector [lobby]="store.lobby()!" (setReactionsEnabled)="store.setReactionsEnabled($event)"/>
        <app-deck-selector [lobby]="store.lobby()!" (setDeck)="store.setDeck($event)"/>
      </div>
    </div>
  `
})
export class SettingsPageComponent {
  readonly store = inject(SessionStore);
}
