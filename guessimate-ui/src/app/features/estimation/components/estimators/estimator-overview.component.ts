import {Component, computed, input, output} from '@angular/core';
import {EstimatorDetailComponent} from './estimator-detail.component';
import {LobbyInfo, UserRole} from '../../../session/models/session.model';

@Component({
  selector: 'app-estimator-overview',
  imports: [
    EstimatorDetailComponent,
  ],
  template: `
    <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
      <div class="flex items-center justify-between py-3 px-4">
        <div class="flex flex-col gap-1">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M15.75 15.75V18m-7.5-6.75h.008v.008H8.25v-.008Zm0 2.25h.008v.008H8.25V13.5Zm0 2.25h.008v.008H8.25v-.008Zm0 2.25h.008v.008H8.25V18Zm2.498-6.75h.007v.008h-.007v-.008Zm0 2.25h.007v.008h-.007V13.5Zm0 2.25h.007v.008h-.007v-.008Zm0 2.25h.007v.008h-.007V18Zm2.504-6.75h.008v.008h-.008v-.008Zm0 2.25h.008v.008h-.008V13.5Zm0 2.25h.008v.008h-.008v-.008Zm0 2.25h.008v.008h-.008V18Zm2.498-6.75h.008v.008h-.008v-.008Zm0 2.25h.008v.008h-.008V13.5ZM8.25 6h7.5v2.25h-7.5V6ZM12 2.25c-1.892 0-3.758.11-5.593.322C5.307 2.7 4.5 3.65 4.5 4.757V19.5a2.25 2.25 0 0 0 2.25 2.25h10.5a2.25 2.25 0 0 0 2.25-2.25V4.757c0-1.108-.806-2.057-1.907-2.185A48.507 48.507 0 0 0 12 2.25Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">Estimators</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Participants who can vote</span>
        </div>
        <div class="flex items-center justify-center p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0 min-w-[2.5rem]">
          <span class="text-lg font-semibold text-gray-500 dark:text-gray-400 leading-none">{{ estimators().length }}</span>
        </div>
      </div>
      <div class="flex flex-col divide-y divide-surface-200 dark:divide-gray-800">
        @for (estimator of estimators(); track estimator.userId) {
          <app-estimator-detail [lobby]="lobby()" [user]="estimator"/>
        }
        @if (!isEstimator()) {
          <button (click)="setUserRole.emit('ESTIMATOR')"
                  class="w-full flex items-center gap-3 py-3 px-4 text-left hover:bg-surface-100 dark:hover:bg-gray-800/60 transition-colors group cursor-pointer">
            <div
              class="shrink-0 flex items-center justify-center size-8 rounded-full bg-surface-200 text-surface-500 dark:bg-gray-800 dark:text-gray-400 group-hover:bg-surface-300 dark:group-hover:bg-gray-700 transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
              </svg>
            </div>
            <span class="font-medium text-sm text-gray-500 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white transition-colors">
              Join as Estimator
            </span>
          </button>
        }
      </div>
    </div>
  `
})
export class EstimatorOverviewComponent {

  lobby = input.required<LobbyInfo>();
  setUserRole = output<UserRole>();

  isEstimator = computed(() => this.lobby().users.find(user => user.self)?.role === 'ESTIMATOR');

  estimators = computed(() => {
    const estimators = this.lobby().users
      .filter(user => user.role === "ESTIMATOR");

    if (this.lobby().status === "ESTIMATION_COMPLETED") {
      const deck = this.lobby().configuration.deck;
      return estimators.sort((left, right) => {
        if (left.estimate === null) {
          return 1;
        }
        if (right.estimate === null) {
          return -1;
        }
        return deck.cards.indexOf(left.estimate) - deck.cards.indexOf(right.estimate);
      });
    }
    return estimators;
  });

}