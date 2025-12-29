import {Component, inject} from '@angular/core';
import {EstimationHistoryCardComponent} from '../components/estimation-history-card.component';
import {SessionStore} from '../../session/services/session.store';

@Component({
  selector: 'app-history-page',
  imports: [EstimationHistoryCardComponent],
  standalone: true,
  template: `
    <div class="grid grid-cols-1 gap-4">
      <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
        <div class="flex flex-col gap-1 py-3 px-4">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">History</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">View the latest estimations for this session.</span>
        </div>

        <div>
          @if (store.estimations().length > 0) {
            <div class="flex flex-col">
              @for (estimation of store.estimations(); track estimation.estimationId) {
                <app-estimation-history-card [estimation]="estimation"/>
              }
            </div>
          } @else {
            <div class="flex flex-col gap-2 items-center p-4">
              <span class="text-lg text-gray-600 dark:text-gray-400">No estimations yet</span>
              <span class="text-sm text-gray-500 dark:text-gray-500">Complete your first estimation to see it here</span>
            </div>
          }
        </div>
      </div>
    </div>
  `
})
export class HistoryPageComponent {
  readonly store = inject(SessionStore);
}
