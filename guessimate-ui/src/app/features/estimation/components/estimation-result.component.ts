import {Component, computed, effect, inject, input, output, signal} from '@angular/core';
import {NgClass} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {Emoji, Estimation, LobbyInfo} from '../../session/models/session.model';
import {ReactionCounterComponent} from './reaction-counter.component';

@Component({
  selector: 'app-estimation-result',
  imports: [NgClass, ReactionCounterComponent],
  template: `
    @if (estimation()) {
      <div class="flex-1 flex flex-col gap-4">
        <div class="flex flex-col divide-y divide-gray-200 dark:divide-gray-800">
          <div class="pt-2">
            <div class="grid grid-cols-3 gap-2 select-none">
              @for (position of [0, 1, 2]; track position) {
                @if (estimation()!.sortedVotesByEstimate[position]) {
                  <div class="flex flex-col items-center justify-center gap-2 p-4 rounded-md border"
                       [ngClass]="{
                         'bg-warning-50 border-warning-300 dark:bg-warning-950/20 dark:border-warning-800': getRank(position) === 0,
                         'bg-surface-100 border-surface-300 dark:bg-gray-800 dark:border-gray-700': getRank(position) === 1,
                         'bg-danger-50 border-danger-300 dark:bg-danger-950/20 dark:border-danger-800': getRank(position) === 2
                       }">
                    <span class="text-4xl font-bold"
                          [ngClass]="{
                            'text-warning-600 dark:text-warning-500': getRank(position) === 0,
                            'text-gray-500 dark:text-gray-400': getRank(position) === 1,
                            'text-danger-600 dark:text-danger-500': getRank(position) === 2
                          }">{{ estimation()!.sortedVotesByEstimate[position][0] }}</span>
                    <div class="flex items-baseline gap-1">
                      <span class="text-lg font-bold text-gray-900 dark:text-white">{{ estimation()!.sortedVotesByEstimate[position][1] }}</span>
                      <span class="text-xs text-gray-500 dark:text-gray-400">vote{{ estimation()!.sortedVotesByEstimate[position][1] !== 1 ? 's' : '' }}</span>
                    </div>
                    <div class="w-full flex flex-col gap-1">
                      <div class="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-1.5">
                        <div class="h-1.5 rounded-full transition-all"
                             [ngClass]="{
                               'bg-warning-600 dark:bg-warning-500': getRank(position) === 0,
                               'bg-gray-500 dark:text-gray-400': getRank(position) === 1,
                               'bg-danger-600 dark:bg-danger-500': getRank(position) === 2
                             }"
                             [style.width.%]="Math.round(estimation()!.sortedVotesByEstimate[position][1] / estimation()!.amountOfParticipants * 100)"></div>
                      </div>
                      <span
                        class="text-xs font-medium text-gray-500 dark:text-gray-400 text-center">{{ Math.round(estimation()!.sortedVotesByEstimate[position][1] / estimation()!.amountOfParticipants * 100) }}
                        %</span>
                    </div>
                  </div>
                } @else {
                  <div class="flex flex-col items-center justify-center gap-1 p-4 rounded-md border border-dashed"
                       [ngClass]="{
                         'border-warning-300 dark:border-warning-800': getRank(position) === 0,
                         'border-surface-300 dark:border-gray-700': getRank(position) === 1,
                         'border-danger-300 dark:border-danger-800': getRank(position) === 2
                       }">
                    <span class="text-4xl font-bold text-gray-300 dark:text-gray-700">-</span>
                    <span class="text-xs text-gray-400 dark:text-gray-600">No votes</span>
                  </div>
                }
              }
            </div>
            <app-reaction-counter [lobby]="lobby()" (sendReaction)="sendReaction.emit($event)"/>
            @if (hasInsights()) {
              <div class="flex flex-wrap items-center justify-center gap-1.5 pt-4 select-none">
                @if (consensus()) {
                  <span class="bg-success-100 text-success-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-success-900 dark:text-success-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
                    </svg>
                    Consensus
                  </span>
                }
                @if (hotStreak()) {
                  <span class="bg-danger-100 text-danger-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-danger-900 dark:text-danger-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M15.362 5.214A8.252 8.252 0 0 1 12 21 8.25 8.25 0 0 1 6.038 7.047 8.287 8.287 0 0 0 9 9.601a8.983 8.983 0 0 1 3.361-6.867 8.21 8.21 0 0 0 3 2.48Z M12 18a3.75 3.75 0 0 0 .495-7.468 5.99 5.99 0 0 0-1.925 3.547 5.975 5.975 0 0 1-2.133-1.001A3.75 3.75 0 0 0 12 18Z"/>
                    </svg>
                    Hot Streak
                  </span>
                }
                @if (soloVote()) {
                  <span class="bg-accent-100 text-accent-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-accent-900 dark:text-accent-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
                    </svg>
                    Solo Vote
                  </span>
                }
                @if (highDisagreement()) {
                  <span class="bg-danger-100 text-danger-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-danger-900 dark:text-danger-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M3 7.5 7.5 3m0 0L12 7.5M7.5 3v13.5m13.5 0L16.5 21m0 0L12 16.5m4.5 4.5V7.5"/>
                    </svg>
                    High Disagreement
                  </span>
                }
                @if (splitDecision()) {
                  <span class="bg-warning-100 text-warning-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-warning-900 dark:text-warning-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M7.5 21 3 16.5m0 0L7.5 12M3 16.5h13.5m0-13.5L21 7.5m0 0L16.5 12M21 7.5H7.5"/>
                    </svg>
                    Split Decision
                  </span>
                }
                @if (outlierPresent()) {
                  <span class="bg-brand-100 text-brand-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-brand-900 dark:text-brand-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 5.25h.008v.008H12v-.008Z"/>
                    </svg>
                    Outlier
                  </span>
                }
                @if (nearConsensus()) {
                  <span class="bg-success-100 text-success-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-success-900 dark:text-success-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M9 12.75 11.25 15 15 9.75m-3-7.036A11.959 11.959 0 0 1 3.598 6 11.99 11.99 0 0 0 3 9.749c0 5.592 3.824 10.29 9 11.623 5.176-1.332 9-6.03 9-11.622 0-1.31-.21-2.571-.598-3.751h-.152c-3.196 0-6.1-1.248-8.25-3.285Z"/>
                    </svg>
                    Near Consensus
                  </span>
                }
                @if (frequentRevote()) {
                  <span class="bg-warning-100 text-warning-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-warning-900 dark:text-warning-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"/>
                    </svg>
                    Frequent Revote
                  </span>
                }
                @if (allUnique()) {
                  <span class="bg-highlight-100 text-highlight-800 text-xs font-medium inline-flex items-center gap-1 px-2 py-1 rounded dark:bg-highlight-900 dark:text-highlight-300">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M9.813 15.904 9 18.75l-.813-2.846a4.5 4.5 0 0 0-3.09-3.09L2.25 12l2.846-.813a4.5 4.5 0 0 0 3.09-3.09L9 5.25l.813 2.846a4.5 4.5 0 0 0 3.09 3.09L15.75 12l-2.846.813a4.5 4.5 0 0 0-3.09 3.09ZM18.259 8.715 18 9.75l-.259-1.035a3.375 3.375 0 0 0-2.455-2.456L14.25 6l1.036-.259a3.375 3.375 0 0 0 2.455-2.456L18 2.25l.259 1.035a3.375 3.375 0 0 0 2.456 2.456L21.75 6l-1.035.259a3.375 3.375 0 0 0-2.456 2.456ZM16.894 20.567 16.5 21.75l-.394-1.183a2.25 2.25 0 0 0-1.423-1.423L13.5 18.75l1.183-.394a2.25 2.25 0 0 0 1.423-1.423l.394-1.183.394 1.183a2.25 2.25 0 0 0 1.423 1.423l1.183.394-1.183.394a2.25 2.25 0 0 0-1.423 1.423Z"/>
                    </svg>
                    All Unique
                  </span>
                }
              </div>
            }
          </div>
        </div>
      </div>
    } @else {
      <div class="flex-1 flex flex-col gap-4">
        <div class="flex flex-col divide-y divide-gray-200 dark:divide-gray-800">
          <div class="pt-2">
            <div class="grid grid-cols-3 gap-2">
              @for (position of [0, 1, 2]; track position) {
                <div class="flex flex-col items-center justify-center gap-2 p-4 rounded-md border animate-pulse"
                     [ngClass]="{
                       'bg-warning-50 border-warning-300 dark:bg-warning-950/20 dark:border-warning-800': position === 0,
                       'bg-surface-200 border-surface-300 dark:bg-gray-800 dark:border-gray-700': position === 1,
                       'bg-danger-50 border-danger-300 dark:bg-danger-950/20 dark:border-danger-800': position === 2
                     }">
                  <div class="w-12 h-10 bg-gray-300 dark:bg-gray-600 rounded"></div>
                  <div class="flex items-baseline">
                    <div class="w-8 h-6 bg-gray-300 dark:bg-gray-600 rounded"></div>
                  </div>
                  <div class="w-full flex flex-col gap-2">
                    <div class="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-1.5"></div>
                    <div class="w-10 h-4 bg-gray-300 dark:bg-gray-600 rounded mx-auto"></div>
                  </div>
                </div>
              }
            </div>
            @if (lobby().configuration.reactionsEnabled) {
              <div class="flex flex-wrap items-center justify-center gap-1.5 pt-4">
                <div class="w-50 h-6 bg-gray-300 dark:bg-gray-600 rounded animate-pulse"></div>
              </div>
            }
            <div class="flex flex-wrap items-center justify-center gap-1.5 pt-4">
              <div class="w-20 h-6 bg-gray-300 dark:bg-gray-600 rounded animate-pulse"></div>
              <div class="w-24 h-6 bg-gray-300 dark:bg-gray-600 rounded animate-pulse"></div>
              <div class="w-16 h-6 bg-gray-300 dark:bg-gray-600 rounded animate-pulse"></div>
            </div>
          </div>
        </div>
      </div>
    }
  `
})
export class EstimationResultComponent {

  protected readonly Math = Math;

  lobby = input.required<LobbyInfo>();
  sendReaction = output<Emoji>();

  private readonly http = inject(HttpClient);

  estimationId = computed(() =>
    this.lobby().status == "ESTIMATING" ? null : {sessionId: this.lobby().sessionId, estimationId: this.lobby().previousEstimationId});
  estimation = signal<EstimationStatistics | null>(null);

  consensus = computed(() => this.estimation()?.insights?.includes("CONSENSUS"));
  hotStreak = computed(() => this.estimation()?.insights?.includes("CONSENSUS_HOT_STREAK"));
  soloVote = computed(() => this.estimation()?.insights?.includes("SOLO_VOTE"));
  highDisagreement = computed(() => this.estimation()?.insights?.includes("HIGH_DISAGREEMENT"));
  splitDecision = computed(() => this.estimation()?.insights?.includes("SPLIT_DECISION"));
  outlierPresent = computed(() => this.estimation()?.insights?.includes("OUTLIER_PRESENT"));
  nearConsensus = computed(() => this.estimation()?.insights?.includes("NEAR_CONSENSUS"));
  frequentRevote = computed(() => this.estimation()?.insights?.includes("FREQUENT_REVOTE"));
  allUnique = computed(() => this.estimation()?.insights?.includes("ALL_UNIQUE"));
  hasInsights = computed(() => this.estimation()?.insights?.length && this.estimation()!.insights.length > 0);

  constructor() {
    effect(() => {
      const estimationId = this.estimationId();
      if (estimationId === null) {
        this.estimation.set(null);
        return;
      }
      this.http.get<Estimation>(`/api/sessions/${estimationId.sessionId}/estimations/${estimationId.estimationId}`)
        .subscribe(estimation => {
          const estimationId = this.estimationId();
          if (estimationId === null) {
            this.estimation.set(null);
            return;
          }
          this.estimation.set({
            ...estimation,
            sortedVotesByEstimate: Object.entries(estimation.votesByEstimate)
              .sort(([left], [right]) => {
                return (estimation.votesByEstimate[right] - estimation.votesByEstimate[left])
                  || (estimation.estimates.indexOf(left) - estimation.estimates.indexOf(right));
              }),
          })
        });
    });
  }

  protected getRank(position: number): number {
    const votesByEstimate = this.estimation()?.sortedVotesByEstimate;
    if (!votesByEstimate || position === 0) {
      return 0;
    }

    let rank = 0;
    for (let index = 1; index <= position; index++) {
      if (votesByEstimate[index] === undefined || votesByEstimate[index - 1] === undefined || votesByEstimate[index][1] < votesByEstimate[index - 1][1]) {
        rank = index;
      }
    }
    return rank;
  }
}

type EstimationStatistics = Estimation & { sortedVotesByEstimate: [string, number][] };
