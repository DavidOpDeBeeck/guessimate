import {Component, computed, inject} from '@angular/core';
import {DebugService} from '../services/debug.service';
import {DebugLogEntryComponent} from '../components/debug-log-entry.component';

@Component({
  selector: 'app-debug-log-page',
  standalone: true,
  imports: [DebugLogEntryComponent],
  template: `
    <div class="grid grid-cols-1 gap-4">
      <div class="flex flex-col bg-surface-100/60 border border-surface-200 dark:bg-gray-900/40 dark:border-gray-800/60 rounded-md shadow-sm divide-y divide-surface-300 dark:divide-gray-800">
        <div class="flex flex-col gap-1 py-3 px-4">
          <div class="flex gap-2 items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6 text-gray-900 dark:text-white">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="m6.75 7.5 3 2.25-3 2.25m4.5 0h3m-9 8.25h13.5A2.25 2.25 0 0 0 21 18V6a2.25 2.25 0 0 0-2.25-2.25H5.25A2.25 2.25 0 0 0 3 6v12a2.25 2.25 0 0 0 2.25 2.25Z"/>
            </svg>
            <h2 class="text-2xl font-semibold leading-none text-gray-900 dark:text-white">WebSocket Debug Log</h2>
          </div>
          <span class="text-sm font-normal text-gray-600 dark:text-gray-400">Real-time WebSocket message log</span>
        </div>

        <div>
          <div class="flex flex-col">
            @for (entry of entries(); track entry.id) {
              <app-debug-log-entry [entry]="entry"/>
            }
          </div>

          @if (entries().length === 0) {
            <div class="flex flex-col gap-2 p-8 items-center justify-center text-center">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-12 text-gray-400">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M20.25 6.375c0 2.278-3.694 4.125-8.25 4.125S3.75 8.653 3.75 6.375m16.5 0c0-2.278-3.694-4.125-8.25-4.125S3.75 4.097 3.75 6.375m16.5 0v11.25c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125V6.375m16.5 0v3.75m-16.5-3.75v3.75m16.5 0v3.75C20.25 16.153 16.556 18 12 18s-8.25-1.847-8.25-4.125v-3.75m16.5 0c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125"/>
              </svg>
              <span class="text-lg text-gray-600 dark:text-gray-400">
                No messages logged yet
              </span>
              <span class="text-sm text-gray-500 dark:text-gray-500">
                WebSocket commands and events will appear here
              </span>
            </div>
          }
        </div>
      </div>
    </div>
  `
})
export class DebugLogPageComponent {

  private debugService = inject(DebugService);

  entries = computed(() =>
    this.debugService.debugLog()
      .slice(0, 10)
      .reverse()
  );
}