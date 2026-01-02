import {Component, computed, input, output} from '@angular/core';
import {LobbyInfo, UserInfo} from '../../../session/models/session.model';
import {InlineUsernameComponent} from '../inline-username.component';

@Component({
  selector: 'app-estimator-detail',
  imports: [InlineUsernameComponent],
  template: `
    <div class="flex items-center justify-between gap-4 py-3 px-4">
      <div class="flex items-center gap-3 min-w-0">
        <div [class]="iconContainerClasses()" class="shrink-0 flex items-center justify-center size-8 rounded-full">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
          </svg>
        </div>
        <app-inline-username
          [username]="user().username"
          [editable]="user().self"
          [highlighted]="user().self"
          (setUsername)="setUsername.emit($event)"
        />
      </div>
      <div class="flex items-center gap-3 shrink-0">
        @if (lobby().status == "ESTIMATION_COMPLETED") {
          <span class="font-mono text-lg font-semibold"
                [class]="user().self ? 'text-brand-600 dark:text-brand-400' : 'text-gray-900 dark:text-white'">
            {{ user().estimate || '-' }}
          </span>
        } @else if (user().estimate) {
          <div class="flex items-center gap-1.5 px-2 py-1 rounded text-xs transition-colors"
               [class]="user().self ? 'bg-brand-100 dark:bg-brand-900/30 text-brand-600 dark:text-brand-400' : 'bg-surface-200 dark:bg-gray-800 text-surface-500 dark:text-gray-400'">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-3">
              <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5"/>
            </svg>
            <span class="font-medium uppercase">Voted</span>
          </div>
        } @else {
          <div class="flex items-center gap-1.5 px-2 py-1 rounded text-xs transition-colors"
               [class]="user().self ? 'bg-brand-100 dark:bg-brand-900/30 text-brand-600 dark:text-brand-400' : 'bg-surface-200 dark:bg-gray-800 text-surface-500 dark:text-gray-400'">
            <svg xmlns='http://www.w3.org/2000/svg' class="size-3" viewBox='0 0 100 100'>
              <path fill='none' stroke='currentColor' stroke-width='12' stroke-dasharray='205.271 51.318'
                    d='M24.3 30C11.4 30 5 43.3 5 50s6.4 20 19.3 20c19.3 0 32.1-40 51.4-40C88.6 30 95 43.3 95 50s-6.4 20-19.3 20C56.4 70 43.6 30 24.3 30z' stroke-linecap='round'>
                <animate attributeName='stroke-dashoffset' repeatCount='indefinite' dur='3s' keyTimes='0;1' values='0;256.589'/>
              </path>
            </svg>
            <span class="font-medium uppercase">Waiting</span>
          </div>
        }
      </div>
    </div>
  `
})
export class EstimatorDetailComponent {

  lobby = input.required<LobbyInfo>();
  user = input.required<UserInfo>();
  setUsername = output<string>();

  iconContainerClasses = computed(() => {
    if (this.user().self) {
      return 'bg-brand-100 text-brand-600 dark:bg-brand-900/30 dark:text-brand-400';
    }
    return 'bg-gray-100 text-gray-500 dark:bg-gray-800 dark:text-gray-400';
  });
}
