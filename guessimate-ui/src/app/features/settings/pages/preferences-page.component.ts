import {Component, inject} from '@angular/core';
import {UsernameSelectorComponent} from '../components/username-selector.component';
import {AppearanceSelectorComponent} from '../components/appearance-selector.component';
import {DebugModeSelectorComponent} from '../components/debug-mode-selector.component';
import {SessionStore} from '../../session/services/session.store';

@Component({
  selector: 'app-preferences-page',
  standalone: true,
  imports: [
    UsernameSelectorComponent,
    AppearanceSelectorComponent,
    DebugModeSelectorComponent
  ],
  template: `
    <div class="grid grid-cols-1 gap-4">
      <div class="flex flex-col bg-surface-100/60 border border-surface-300 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
        <div class="flex flex-col gap-1 py-3 px-4">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">Preferences</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Manage your personal preferences.</span>
        </div>
        <app-username-selector [lobby]="store.lobby()!" (setUsername)="store.setUsername($event)"/>
        <app-appearance-selector/>
        <app-debug-mode-selector/>
      </div>
    </div>
  `
})
export class PreferencesPageComponent {
  readonly store = inject(SessionStore);
}


