import {Component, inject} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {DebugService} from '../../features/debug/services/debug.service';
import {CopyUrlButtonComponent} from './copy-url-button.component';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    CopyUrlButtonComponent
  ],
  template: `
    <div class="w-full px-4 py-2 md:mb-4">
      <div class="flex flex-col md:flex-row mb:items-center justify-between">
        <a routerLink="/" class="cursor-pointer">
          <h1 class="text-5xl font-bold tracking-tight text-gray-900 dark:text-white">Guessimate</h1>
          <span class="text-xl text-gray-400 dark:text-gray-500">Estimate together. Decide smarter.</span>
        </a>

        <div class="flex items-center gap-2 py-2">
          <nav class="flex items-center gap-1">
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
            @if (debugService.debugMode()) {
              <a routerLink="debug"
                 routerLinkActive="bg-brand-100 text-brand-700 dark:bg-gray-800 dark:text-white pointer-events-none"
                 class="flex items-center gap-2 px-3 py-2 text-sm font-medium text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer"
                 title="Debug">
                Debug
              </a>
            }
            <a routerLink="settings"
               routerLinkActive="bg-brand-100 text-brand-700 dark:bg-gray-800 dark:text-white pointer-events-none"
               class="flex items-center gap-2 px-3 py-2 text-sm font-medium text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer"
               title="Settings">
              Settings
            </a>
          </nav>
          <div class="w-px h-6 bg-surface-300 dark:bg-gray-700"></div>
          <app-copy-url-button/>
        </div>
      </div>
    </div>
  `
})
export class NavigationComponent {
  public readonly debugService = inject(DebugService);
}
