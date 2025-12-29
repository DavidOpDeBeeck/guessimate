import {Component, computed, input, output} from '@angular/core';
import {ObserverDetailComponent} from './observer-detail.component';
import {LobbyInfo, UserRole} from '../../../session/models/session.model';

@Component({
  selector: 'app-observer-overview',
  imports: [
    ObserverDetailComponent,
  ],
  template: `
    <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
      <div class="flex items-center justify-between py-3 px-4">
        <div class="flex flex-col gap-1">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"/>
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">Observers</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Participants who can watch but not vote</span>
        </div>
        <div class="flex items-center justify-center p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0 min-w-[2.5rem]">
          <span class="text-lg font-semibold text-gray-500 dark:text-gray-400 leading-none">{{ observers().length }}</span>
        </div>
      </div>
      <div class="flex flex-col divide-y divide-surface-200 dark:divide-gray-800">
        @for (observer of observers(); track observer.userId) {
          <app-observer-detail [user]="observer"/>
        }
        @if (!isObserver()) {
          <button (click)="setUserRole.emit('OBSERVER')"
                  class="w-full flex items-center gap-3 py-3 px-4 text-left hover:bg-surface-100 dark:hover:bg-gray-800/50 transition-colors group cursor-pointer">
            <div
              class="shrink-0 flex items-center justify-center size-8 rounded-full bg-surface-200 text-surface-500 dark:bg-gray-800 dark:text-gray-400 group-hover:bg-surface-300 dark:group-hover:bg-gray-700 transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
              </svg>
            </div>
            <span class="font-medium text-sm text-gray-500 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white transition-colors">
              Join as Observer
            </span>
          </button>
        }
      </div>
    </div>
  `
})
export class ObserverOverviewComponent {

  lobby = input.required<LobbyInfo>();
  setUserRole = output<UserRole>();

  isObserver = computed(() => this.lobby().users.find(user => user.self)?.role === 'OBSERVER');

  observers = computed(() => this.lobby().users.filter(user => user.role === "OBSERVER"));

}