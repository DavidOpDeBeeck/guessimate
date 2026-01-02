import {Component, inject, OnInit, signal} from '@angular/core';
import {RouterLink} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {LobbyMetrics} from '../models/lobby-metrics.model';

@Component({
  selector: 'app-metrics-page',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
    <div class="w-full flex flex-col items-center">
      <div class="w-full max-w-6xl flex flex-col pt-4 pb-2 px-4">
        <div class="py-3 mb-2 md:mb-4">
          <a routerLink="/" class="cursor-pointer">
            <h1 class="text-5xl font-bold tracking-tight text-gray-900 dark:text-white">Guessimate</h1>
            <span class="text-xl text-gray-400 dark:text-gray-500">Live overview of estimation activity.</span>
          </a>
        </div>

        @if (metrics(); as data) {
          <div class="flex flex-col gap-4">
            <!-- Live Metrics -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm p-4">
                <div class="flex items-center gap-3 mb-3">
                  <div class="p-2 bg-brand-50 dark:bg-brand-900/20 rounded-md">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-brand-600 dark:text-brand-400">
                      <path stroke-linecap="round" stroke-linejoin="round" d="m21 7.5-9-5.25L3 7.5m18 0-9 5.25m9-5.25v9l-9 5.25M3 7.5l9 5.25M3 7.5v9l9 5.25m0-9v9"/>
                    </svg>
                  </div>
                  <span class="text-sm font-medium text-gray-500 dark:text-gray-400">Active Lobbies</span>
                </div>
                <div class="flex items-baseline gap-2">
                  <span class="text-3xl font-bold text-gray-900 dark:text-white">{{ data.activeLobbies }}</span>
                  <span class="text-xs text-success-600 dark:text-success-400 font-medium">live</span>
                </div>
              </div>
              <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm p-4">
                <div class="flex items-center gap-3 mb-3">
                  <div class="p-2 bg-success-50 dark:bg-success-900/20 rounded-md">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-success-600 dark:text-success-400">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M15 19.128a9.38 9.38 0 0 0 2.625.372 9.337 9.337 0 0 0 4.121-.952 4.125 4.125 0 0 0-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 0 1 8.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0 1 11.964-3.07M12 6.375a3.375 3.375 0 1 1-6.75 0 3.375 3.375 0 0 1 6.75 0Zm8.25 2.25a2.625 2.625 0 1 1-5.25 0 2.625 2.625 0 0 1 5.25 0Z"/>
                    </svg>
                  </div>
                  <span class="text-sm font-medium text-gray-500 dark:text-gray-400">Users Estimating</span>
                </div>
                <div class="flex items-baseline gap-2">
                  <span class="text-3xl font-bold text-gray-900 dark:text-white">{{ data.connectedUsers }}</span>
                  <span class="text-xs text-success-600 dark:text-success-400 font-medium">online</span>
                </div>
              </div>
              <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm p-4">
                <div class="flex items-center gap-3 mb-3">
                  <div class="p-2 bg-amber-50 dark:bg-amber-900/20 rounded-md">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-amber-600 dark:text-amber-400">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
                    </svg>
                  </div>
                  <span class="text-sm font-medium text-gray-500 dark:text-gray-400">Total Estimations</span>
                </div>
                <div class="flex items-baseline gap-2">
                  <span class="text-3xl font-bold text-gray-900 dark:text-white">{{ data.completedEstimations }}</span>
                  <span class="text-xs text-gray-400 dark:text-gray-500">all time</span>
                </div>
              </div>
            </div>

            <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm p-4">
              <div class="flex items-center gap-3 mb-4">
                <div class="p-2 bg-gray-100 dark:bg-gray-800 rounded-md">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-500 dark:text-gray-400">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
                  </svg>
                </div>
                <span class="text-sm font-medium text-gray-500 dark:text-gray-400">Historical Insights</span>
              </div>
              <div class="flex flex-col items-center justify-center p-8 min-h-48">
                <div class="w-12 h-12 bg-surface-200 dark:bg-gray-800 rounded-full flex items-center justify-center mb-3">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-400">
                    <path stroke-linecap="round" stroke-linejoin="round"
                          d="M9.75 3.104v5.714a2.25 2.25 0 0 1-.659 1.591L5 14.5M9.75 3.104c-.251.023-.501.05-.75.082m.75-.082a24.301 24.301 0 0 1 4.5 0m0 0v5.714c0 .597.237 1.17.659 1.591L19.8 15.3M14.25 3.104c.251.023.501.05.75.082M19.8 15.3l-1.57.393A9.065 9.065 0 0 1 12 15a9.065 9.065 0 0 0-6.23-.693L5 14.5m14.8.8 1.402 1.402c1.232 1.232.65 3.318-1.067 3.611A48.309 48.309 0 0 1 12 21c-2.773 0-5.491-.235-8.135-.687-1.718-.293-2.3-2.379-1.067-3.61L5 14.5"/>
                  </svg>
                </div>
                <p class="text-gray-500 dark:text-gray-400 text-center">Detailed charts and historical usage trends are coming soon.</p>
              </div>
            </div>
          </div>
        }
      </div>
    </div>
  `
})
export class MetricsPageComponent implements OnInit {
  private readonly http = inject(HttpClient);
  readonly metrics = signal<LobbyMetrics | null>(null);

  ngOnInit() {
    this.http.get<LobbyMetrics>('/api/metrics').subscribe(data => this.metrics.set(data));
  }
}
