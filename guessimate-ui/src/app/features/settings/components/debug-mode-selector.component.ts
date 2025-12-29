import {Component, inject} from '@angular/core';
import {DebugService} from '../../debug/services/debug.service';

@Component({
  selector: 'app-debug-mode-selector',
  standalone: true,
  template: `
    <div class="flex items-center justify-between py-3 px-4">
      <div class="flex gap-3 items-center">
        <div class="p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-900 dark:text-white">
            <path stroke-linecap="round" stroke-linejoin="round" d="M17.25 6.75 22.5 12l-5.25 5.25m-10.5 0L1.5 12l5.25-5.25m7.5-3-4.5 16.5"/>
          </svg>
        </div>
        <div class="flex flex-col">
          <span class="text-sm font-medium text-gray-900 dark:text-white">Developer Tools</span>
          <span class="text-xs text-gray-500 dark:text-gray-400">Enable advanced logs</span>
        </div>
      </div>
      <div class="inline-flex rounded-md bg-surface-200 dark:bg-gray-800 p-0.5 shrink-0">
        <button type="button"
                (click)="setDebugMode(false)"
                [class]="!debugMode()
                    ? 'px-2 py-1 text-xs font-medium rounded bg-surface-50 dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Disabled
        </button>
        <button type="button"
                (click)="setDebugMode(true)"
                [class]="debugMode()
                    ? 'px-2 py-1 text-xs font-medium rounded bg-brand-600 text-white shadow-sm cursor-pointer'
                    : 'px-2 py-1 text-xs font-medium rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
          Enabled
        </button>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: contents
    }
  `]
})
export class DebugModeSelectorComponent {
  public readonly debugService = inject(DebugService);

  setDebugMode(enabled: boolean) {
    this.debugService.setDebugMode(enabled);
  }

  debugMode() {
    return this.debugService.debugMode();
  }
}
