import {Component, input, output} from '@angular/core';
import {EstimationResultComponent} from './estimation-result.component';
import {RevealEstimationButtonComponent} from './reveal-estimation-button.component';
import {AutoRevealStatusComponent} from './auto-reveal-status.component';
import {TimerStatusComponent} from './timer-status.component';
import {Emoji, LobbyInfo} from '../../session/models/session.model';

@Component({
  selector: 'app-estimation-lifecycle',
  imports: [
    EstimationResultComponent,
    RevealEstimationButtonComponent,
    AutoRevealStatusComponent,
    TimerStatusComponent,
  ],
  template: `
    <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
      <div class="flex items-center justify-between py-3 px-4">
        <div class="flex flex-col gap-1">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 0 1 3 19.875v-6.75ZM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V8.625ZM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V4.125Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">Estimation</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Manage and view the active estimation</span>
        </div>
      </div>
      <div class="flex flex-col gap-4 p-4">
        <app-estimation-result [lobby]="lobby()" (sendReaction)="sendReaction.emit($event)"/>
        @if (lobby().status === "ESTIMATION_COMPLETED") {
          <button (click)="startEstimation.emit()"
                  class="w-full text-white bg-brand-600 hover:bg-brand-700 focus:ring-4 focus:outline-none focus:ring-brand-300 dark:bg-brand-600 dark:hover:bg-brand-700 dark:focus:ring-brand-800 font-semibold rounded-lg text-lg px-5 py-2.5 text-center cursor-pointer transition-colors">
            New Estimation
          </button>
        } @else {
          <app-reveal-estimation-button [lobby]="lobby()" (revealEstimation)="revealEstimation.emit()"/>
        }
        <div class="flex items-center justify-center gap-2">
          <app-timer-status [lobby]="lobby()"/>
          <app-auto-reveal-status [lobby]="lobby()"/>
        </div>
      </div>
    </div>
  `
})
export class EstimationLifecycleComponent {

  lobby = input.required<LobbyInfo>();
  startEstimation = output();
  revealEstimation = output();
  sendReaction = output<Emoji>();

}