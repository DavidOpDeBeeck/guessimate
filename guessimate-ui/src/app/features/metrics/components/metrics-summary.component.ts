import {Component, inject, OnInit, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LobbyMetrics} from '../models/lobby-metrics.model';

@Component({
  selector: 'app-metrics-summary',
  standalone: true,
  template: `
    @if (metrics(); as data) {
      <div
        class="mt-12 flex flex-col sm:flex-row items-center justify-center bg-surface-100/60 dark:bg-gray-900/40 divide-y sm:divide-y-0 sm:divide-x divide-surface-300 dark:divide-gray-800 border border-surface-200 dark:border-gray-800/60 rounded-3xl sm:rounded-full overflow-hidden">

        <div class="flex items-center gap-3 px-4 py-2 sm:py-4 w-full sm:w-auto">
          <div class="p-2 bg-brand-50 dark:bg-brand-900/20 rounded-lg text-brand-600 dark:text-brand-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"/>
            </svg>
          </div>
          <div>
            <p class="text-lg font-bold text-gray-900 dark:text-white leading-none">{{ data.activeLobbies }}</p>
            <p class="text-[10px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-tight">Active Sessions</p>
          </div>
        </div>
        <div class="flex items-center gap-3 px-4 py-2 sm:py-4 w-full sm:w-auto">
          <div class="p-2 bg-success-50 dark:bg-success-900/20 rounded-lg text-success-600 dark:text-success-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
            </svg>
          </div>
          <div>
            <p class="text-lg font-bold text-gray-900 dark:text-white leading-none">{{ data.connectedUsers }}</p>
            <p class="text-[10px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-tight">Users Estimating</p>
          </div>
        </div>
        <div class="flex items-center gap-3 px-4 py-2 sm:py-4 w-full sm:w-auto">
          <div class="p-2 bg-accent-50 dark:bg-accent-900/20 rounded-lg text-accent-600 dark:text-accent-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
          </div>
          <div>
            <p class="text-lg font-bold text-gray-900 dark:text-white leading-none">{{ data.completedEstimations }}</p>
            <p class="text-[10px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-tight">Total Estimations</p>
          </div>
        </div>
      </div>
    }
  `
})
export class MetricsSummaryComponent implements OnInit {
  private readonly http = inject(HttpClient);
  readonly metrics = signal<LobbyMetrics | null>(null);

  ngOnInit() {
    this.http.get<LobbyMetrics>('/api/metrics').subscribe(data => this.metrics.set(data));
  }
}
