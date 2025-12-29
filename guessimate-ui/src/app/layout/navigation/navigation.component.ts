import {Component, inject} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {DebugService} from '../../features/debug/services/debug.service';
import {SessionStore} from '../../features/session/services/session.store';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  template: `
    <div class="w-full px-4 py-3 md:mb-4">
      <div class="flex flex-col md:flex-row gap-4 justify-between">
        <a routerLink="/" class="cursor-pointer">
          <h1 class="text-5xl font-bold tracking-tight text-gray-900 dark:text-white">Guessimate</h1>
          <span class="text-xl text-gray-400 dark:text-gray-500">Estimate together. Decide smarter.</span>
        </a>
        <nav class="flex flex-wrap items-center justify-between gap-1">
          <div class="flex items-center gap-1">
            <a routerLink="."
                [routerLinkActiveOptions]="{exact: true}"
                routerLinkActive="bg-brand-100 text-brand-700 dark:bg-gray-800 dark:text-white pointer-events-none"
                class="flex items-center gap-2 px-3 py-2 text-sm font-medium text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer">
              Lobby
            </a>
            <a routerLink="history"
                routerLinkActive="bg-brand-100 text-brand-700 dark:bg-gray-800 dark:text-white pointer-events-none"
                class="flex items-center gap-2 px-3 py-2 text-sm font-medium text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer">
              History
            </a>
          </div>
          <div class="flex items-center gap-1">
            @if (debugService.debugMode()) {
              <a routerLink="debug"
                  routerLinkActive="bg-brand-100 text-brand-700 dark:bg-gray-800 dark:text-white pointer-events-none"
                  class="flex items-center justify-center p-2 text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer"
                  title="Debug">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="m6.75 7.5 3 2.25-3 2.25m4.5 0h3m-9 8.25h13.5A2.25 2.25 0 0 0 21 18V6a2.25 2.25 0 0 0-2.25-2.25H5.25A2.25 2.25 0 0 0 3 6v12a2.25 2.25 0 0 0 2.25 2.25Z" />
                </svg>
                <span class="sr-only">Debug</span>
              </a>
            }
            <a routerLink="settings"
                routerLinkActive="bg-brand-100 text-brand-700 dark:bg-gray-800 dark:text-white pointer-events-none"
                class="flex items-center justify-center p-2 text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer"
                title="Session settings">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 0 1 1.37.49l1.296 2.247a1.125 1.125 0 0 1-.26 1.431l-1.003.827c-.293.24-.438.613-.431.992a6.759 6.759 0 0 1 0 .255c-.007.378.138.75.43.99l1.005.828c.424.35.534.954.26 1.43l-1.298 2.247a1.125 1.125 0 0 1-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 0 1-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 0 1-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 0 1-1.369-.49l-1.297-2.247a1.125 1.125 0 0 1 .26-1.431l1.004-.827c.292-.24.437-.613.43-.992a6.932 6.932 0 0 1 0-.255c.007-.378-.138-.75-.43-.99l-1.004-.828a1.125 1.125 0 0 1-.26-1.43l1.297-2.247a1.125 1.125 0 0 1 1.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.281Z"/>
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"/>
              </svg>
              <span class="sr-only">Settings</span>
            </a>
            @if (store.lobby()) {
              <a routerLink="preferences"
                  routerLinkActive="bg-brand-100 text-brand-700 dark:bg-gray-800 dark:text-white pointer-events-none"
                  class="flex items-center justify-center p-2 text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer"
                  title="User settings">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5">
                  <path stroke-linecap="round" stroke-linejoin="round"
                        d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
                </svg>
                <span class="sr-only">Preferences</span>
              </a>
            }
          </div>
        </nav>
      </div>
    </div>
  `
})
export class NavigationComponent {
  public readonly debugService = inject(DebugService);
  public readonly store = inject(SessionStore);
}
