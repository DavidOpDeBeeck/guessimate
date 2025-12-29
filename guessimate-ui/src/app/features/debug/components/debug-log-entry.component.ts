import {Component, computed, input, signal} from '@angular/core';
import {DebugLogEntry} from '../services/debug.service';
import {DatePipe, JsonPipe} from '@angular/common';

@Component({
  selector: 'app-debug-log-entry',
  standalone: true,
  imports: [DatePipe, JsonPipe],
  styles: [`
    :host {
      display: block;
      animation: fade-in 1s ease-in-out;
    }
    @keyframes fade-in {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }
  `],
  template: `
    <div class="group py-3 px-4 cursor-pointer hover:bg-surface-100 dark:hover:bg-gray-800/50 transition-colors"
         (click)="toggleExpanded()">

      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3 min-w-0">

          <div [class]="iconContainerClasses()" class="shrink-0 flex items-center justify-center size-8 rounded-full bg-opacity-20">
            @if (entry().direction === 'SENT') {
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 10.5 12 3m0 0 7.5 7.5M12 3v18"/>
              </svg>
            } @else {
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 13.5 12 21m0 0-7.5-7.5M12 21V3"/>
              </svg>
            }
          </div>

          <div class="flex flex-col min-w-0">
            <span class="font-medium text-sm text-gray-900 dark:text-white truncate" [title]="entry().name">
              {{ entry().name }}
            </span>
            <div class="flex items-center gap-1.5 text-xs text-gray-500 dark:text-gray-400">
              <span class="font-mono">{{ entry().timestamp | date:'HH:mm:ss.SSS' }}</span>
              <span>•</span>
              <span class="font-medium" [class]="textTypeClass()">{{ entry().messageType }}</span>
            </div>
          </div>
        </div>

        <div class="flex items-center gap-3 shrink-0">
          @if (entry().userId) {
            <div class="hidden sm:flex items-center gap-1.5 px-2 py-1 rounded bg-surface-200 dark:bg-gray-800 text-xs text-gray-600 dark:text-gray-300">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
              </svg>
              <span class="font-mono">{{ entry().userId!.substring(0, 8) }}</span>
            </div>
          }

          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"
               class="size-4 text-gray-400 dark:text-gray-500 group-hover:text-gray-600 dark:group-hover:text-gray-300 transition-all"
               [class.rotate-90]="expanded()">
            <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5"/>
          </svg>
        </div>
      </div>
      @if (expanded()) {
        <div class="bg-surface-100 dark:bg-gray-800/30 p-4 mt-2 rounded-md">
          <pre class="text-xs font-mono overflow-x-auto text-gray-700 dark:text-gray-300 leading-relaxed">{{ entry().payload | json }}</pre>
        </div>
      }
    </div>
  `
})
export class DebugLogEntryComponent {

  entry = input.required<DebugLogEntry>();
  expanded = signal(false);

  iconContainerClasses = computed(() => {
    if (this.entry().direction === 'SENT') {
      return 'bg-brand-100 text-brand-600 dark:bg-brand-900/30 dark:text-brand-400';
    }
    return 'bg-success-100 text-success-600 dark:bg-success-900/30 dark:text-success-400';
  });

  textTypeClass = computed(() => {
    if (this.entry().messageType === 'COMMAND') {
      return 'text-brand-600 dark:text-brand-400';
    }
    return 'text-success-600 dark:text-success-400';
  });

  toggleExpanded() {
    this.expanded.update(value => !value);
  }
}