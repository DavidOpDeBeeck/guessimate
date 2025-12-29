import {Component, computed, input, signal} from '@angular/core';
import {DatePipe} from '@angular/common';
import {Estimation} from '../../session/models/session.model';

@Component({
  selector: 'app-estimation-history-card',
  imports: [DatePipe],
  template: `
    <div class="group py-3 px-4 cursor-pointer hover:bg-surface-100 dark:hover:bg-gray-800/50 transition-colors"
         (click)="toggleExpanded()">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3 min-w-0">

          <div [class]="iconContainerClasses()" class="shrink-0 flex items-center justify-center size-8 rounded-full bg-opacity-20">
            @if (consensus()) {
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5"/>
              </svg>
            } @else {
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12"/>
              </svg>
            }
          </div>

          <div class="flex flex-col min-w-0">
            <span class="font-medium text-sm text-gray-900 dark:text-white truncate">
              Result: {{ mostVoted().estimates.join('-') }}
            </span>
            <div class="flex items-center gap-1.5 text-xs text-gray-500 dark:text-gray-400">
              <span class="font-mono">{{ estimation().timestamp | date:'HH:mm:ss' }}</span>
              <span>•</span>
              <div class="flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                  <path stroke-linecap="round" stroke-linejoin="round"
                        d="M15 19.128a9.38 9.38 0 0 0 2.625.372 9.337 9.337 0 0 0 4.121-.952 4.125 4.125 0 0 0-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 0 1 8.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0 1 11.964-3.07M12 6.375a3.375 3.375 0 1 1-6.75 0 3.375 3.375 0 0 1 6.75 0Zm8.25 2.25a2.625 2.625 0 1 1-5.25 0 2.625 2.625 0 0 1 5.25 0Z"/>
                </svg>
                <span class="font-mono">{{ estimation().amountOfParticipants }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="flex items-center gap-3 shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"
               class="size-4 text-gray-400 dark:text-gray-500 group-hover:text-gray-600 dark:group-hover:text-gray-300 transition-all"
               [class.rotate-90]="expanded()">
            <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5"/>
          </svg>
        </div>
      </div>

      @if (expanded()) {
        <div class="bg-surface-100 dark:bg-gray-800/30 p-4 mt-2 rounded-md">
          <div class="flex flex-col gap-6">
            <!-- Vote Distribution -->
            <div class="flex flex-col gap-3">
              <h3 class="text-xs font-semibold text-gray-500 dark:text-gray-400">Vote Distribution</h3>
              <div class="flex flex-col gap-2">
                @for (entry of sortedVotes(); track entry[0]) {
                  <div class="flex items-center gap-3">
                    <div class="w-8 shrink-0 flex justify-end">
                      <span class="text-sm font-bold text-gray-900 dark:text-white">{{ entry[0] }}</span>
                    </div>
                    <div class="flex-1 h-2 bg-gray-200 dark:bg-gray-700 rounded-full overflow-hidden">
                      <div class="h-full bg-gray-400 dark:bg-gray-500 transition-all duration-500"
                           [style.width.%]="getPercentage(entry[1])"></div>
                    </div>
                    <div class="w-16 shrink-0 text-right">
                      <span class="text-xs font-medium text-gray-600 dark:text-gray-400">{{ entry[1] }} ({{ getPercentage(entry[1]) }}%)</span>
                    </div>
                  </div>
                }
              </div>
            </div>

            <!-- Insights -->
            @if (hasInsights()) {
              <div class="flex flex-col gap-3">
                <h3 class="text-xs font-semibold text-gray-500 dark:text-gray-400">Insights</h3>
                <div class="flex flex-wrap gap-1.5">
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
              </div>
            }
          </div>
        </div>
      }
    </div>
  `,
  styles: []
})
export class EstimationHistoryCardComponent {

  estimation = input.required<Estimation>();
  expanded = signal(false);

  mostVoted = computed(() => {
    const votes = Object.values(this.estimation().votesByEstimate);
    const mostNumberOfVotes = Math.max(...votes);

    const estimates = Object.entries(this.estimation().votesByEstimate)
      .filter(([, votes]) => votes === mostNumberOfVotes)
      .map(([estimate]) => estimate);

    return {
      votes: mostNumberOfVotes,
      estimates: estimates.sort((left, right) =>
        this.estimation().estimates.indexOf(left) - this.estimation().estimates.indexOf(right)
      ),
      percentage: Math.round(mostNumberOfVotes / this.estimation().amountOfParticipants * 100)
    };
  });

  sortedVotes = computed(() => {
    return Object.entries(this.estimation().votesByEstimate)
      .sort(([leftEstimate, leftVotes], [rightEstimate, rightVotes]) => {
        // Sort by votes descending, then by estimate position
        if (rightVotes !== leftVotes) {
          return rightVotes - leftVotes;
        }
        return this.estimation().estimates.indexOf(leftEstimate) - this.estimation().estimates.indexOf(rightEstimate);
      });
  });

  consensus = computed(() => {
    return this.estimation().insights?.includes("CONSENSUS");
  });

  hotStreak = computed(() => {
    return this.estimation().insights?.includes("CONSENSUS_HOT_STREAK");
  });

  soloVote = computed(() => {
    return this.estimation().insights?.includes("SOLO_VOTE");
  });

  highDisagreement = computed(() => {
    return this.estimation().insights?.includes("HIGH_DISAGREEMENT");
  });

  splitDecision = computed(() => {
    return this.estimation().insights?.includes("SPLIT_DECISION");
  });

  outlierPresent = computed(() => {
    return this.estimation().insights?.includes("OUTLIER_PRESENT");
  });

  nearConsensus = computed(() => {
    return this.estimation().insights?.includes("NEAR_CONSENSUS");
  });

  frequentRevote = computed(() => {
    return this.estimation().insights?.includes("FREQUENT_REVOTE");
  });

  allUnique = computed(() => {
    return this.estimation().insights?.includes("ALL_UNIQUE");
  });

  hasInsights = computed(() => {
    return this.estimation().insights?.length > 0;
  });

  iconContainerClasses = computed(() => {
    if (this.consensus()) {
      return 'bg-success-100 text-success-600 dark:bg-success-900/30 dark:text-success-400';
    }
    return 'bg-surface-200 text-surface-500 dark:bg-gray-800 dark:text-gray-400';
  });

  toggleExpanded() {
    this.expanded.update(value => !value);
  }

  getPercentage(votes: number): number {
    return Math.round((votes / this.estimation().amountOfParticipants) * 100);
  }
}