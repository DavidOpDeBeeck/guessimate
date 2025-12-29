import {Component, computed, input, output} from '@angular/core';
import {LobbyInfo} from '../../session/models/session.model';

@Component({
  selector: 'app-estimation-cards',
  template: `
    @if (lobby().status == "ESTIMATING" && isEstimator()) {
      <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
        <div class="flex items-center justify-between py-3 px-4">
          <div class="flex flex-col gap-1">
            <div class="flex gap-2 items-center">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M10.05 4.575a1.575 1.575 0 1 0-3.15 0v3m3.15-3v-1.5a1.575 1.575 0 0 1 3.15 0v1.5m-3.15 0 .075 5.925m3.075.75V4.575m0 0a1.575 1.575 0 0 1 3.15 0V15M6.9 7.575a1.575 1.575 0 1 0-3.15 0v8.175a6.75 6.75 0 0 0 6.75 6.75h2.018a5.25 5.25 0 0 0 3.712-1.538l1.732-1.732a5.25 5.25 0 0 0 1.538-3.712l.003-2.024a.668.668 0 0 1 .198-.471 1.575 1.575 0 1 0-2.228-2.228 3.818 3.818 0 0 0-1.12 2.687M6.9 7.575V12m6.27 4.318A4.49 4.49 0 0 1 16.35 15m.002 0h-.002"/>
              </svg>
              <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">Your Estimate</h2>
            </div>
            <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Pick a card to cast your vote</span>
          </div>
        </div>
        <div class="p-4">
          <div class="grid grid-cols-3 gap-2">
            @for (card of lobby().configuration.deck.cards; track card) {
              @if (card === currentEstimate()) {
                <div (click)="clearEstimate.emit()"
                     class="flex justify-center items-center p-4 text-brand-600! dark:text-brand-400! border-brand-200! dark:border-brand-800! bg-brand-50! dark:bg-brand-900/20! border rounded-lg cursor-pointer select-none transition-colors">
                  <span class="text-xl font-bold tracking-tight">{{ card }}</span>
                </div>
              } @else {
                <button (click)="setEstimate.emit(card)"
                        type="button"
                        class="flex justify-center items-center p-4 bg-surface-50 border border-surface-200 rounded-lg shadow-sm hover:bg-surface-100 dark:bg-gray-900/40 dark:border-gray-800/60 dark:hover:bg-gray-800/60 cursor-pointer select-none transition-colors">
                  <span class="text-xl font-bold tracking-tight text-gray-900 dark:text-white">{{ card }}</span>
                </button>
              }
            }
          </div>
        </div>
      </div>
    }
  `,
  styles: [`
    :host {
      display: contents
    }
  `]
})
export class EstimationCardsComponent {

  lobby = input.required<LobbyInfo>();
  setEstimate = output<string>();
  clearEstimate = output();

  isEstimator = computed(() => this.lobby().users.find(user => user.self)?.role === 'ESTIMATOR');
  currentEstimate = computed(() => this.lobby().users.find(user => user.self)?.estimate ?? null);
}