import {Component, computed, input, OnDestroy, OnInit, output, signal} from '@angular/core';
import {LobbyInfo} from '../../session/models/session.model';

@Component({
  selector: 'app-reveal-estimation-button',
  template: `
    <button (click)="revealEstimation.emit()"
            [disabled]="estimators() == 0 || votesRemaining() == estimators()"
            [class]="estimators() == 0
              ? 'w-full cursor-not-allowed text-surface-500 bg-surface-200 dark:bg-gray-800 dark:text-gray-400 font-semibold rounded-lg text-lg px-5 py-2.5 text-center'
              : 'w-full disabled:cursor-not-allowed disabled:opacity-50 text-white bg-brand-600 enabled:hover:bg-brand-700 focus:ring-4 focus:outline-none focus:ring-brand-300 dark:bg-brand-600 enabled:dark:hover:bg-brand-700 dark:focus:ring-brand-800 font-semibold rounded-lg text-lg px-5 py-2.5 text-center cursor-pointer'">
      @if (estimators() == 0) {
        Need at least one estimator
      } @else if (votesRemaining() == 0) {
        Reveal Estimation{{ timerText() }}
      } @else if (votesRemaining() == 1) {
        1 vote remaining{{ timerText() }}
      } @else {
        {{ votesRemaining() + " votes remaining" + timerText() }}
      }
    </button>
  `
})
export class RevealEstimationButtonComponent implements OnInit, OnDestroy {

  lobby = input.required<LobbyInfo>();
  revealEstimation = output();

  now = signal(Date.now());

  timeRemainingInSeconds = computed(() => {
    const expiresAt = this.lobby().timerExpiresAt;
    if (!expiresAt) {
      return null;
    }

    const expiryTime = new Date(expiresAt).getTime();
    return Math.max(0, Math.floor((expiryTime - this.now()) / 1000));
  });
  timerText = computed(() => {
    const seconds = this.timeRemainingInSeconds();
    if (seconds === null) {
      return '';
    }

    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return ` (${mins}:${secs.toString().padStart(2, '0')})`;
  });

  votesRemaining = computed(() => this.lobby().users
    .filter(user => user.role == "ESTIMATOR")
    .filter(user => !user.estimate)
    .length)

  estimators = computed(() => this.lobby().users
    .filter(user => user.role == "ESTIMATOR")
    .length);

  intervalId?: number;

  ngOnInit() {
    this.intervalId = window.setInterval(() => this.now.set(Date.now()), 1000);
  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }
}
