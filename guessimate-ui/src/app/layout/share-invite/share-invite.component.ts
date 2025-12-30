import {Component, computed, input, signal} from '@angular/core';

type CopyState = 'idle' | 'copied' | 'sliding-out';

@Component({
  selector: 'app-share-invite',
  standalone: true,
  template: `
    <button (click)="copySessionUrl()"
            class="flex items-center gap-1 p-2 rounded-md transition-all cursor-pointer overflow-hidden relative"
            [class]="showCopied() ? 'bg-success-100 text-success-700 dark:bg-success-900/30 dark:text-success-400' : 'text-gray-600 hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white'"
            [title]="showCopied() ? 'Link copied!' : 'Copy invite link'">

      @if (showCopied()) {
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-success-600 shrink-0">
          <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5"/>
        </svg>
        <span class="text-sm font-medium text-success-600 whitespace-nowrap"
              [class.animate-slide-in]="copyState() === 'copied'"
              [class.animate-slide-out]="copyState() === 'sliding-out'">
          Copied!
        </span>
      } @else {
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 shrink-0">
          <path stroke-linecap="round" stroke-linejoin="round"
                d="M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244"/>
        </svg>
      }

      <span class="sr-only">{{ showCopied() ? 'Link copied!' : 'Copy invite link' }}</span>
    </button>
  `,
  styles: [`
    @keyframes slideIn {
      from {
        opacity: 0;
        transform: translateX(-10px);
        max-width: 0;
      }
      to {
        opacity: 1;
        transform: translateX(0);
        max-width: 100px;
      }
    }

    @keyframes slideOut {
      from {
        opacity: 1;
        transform: translateX(0);
        max-width: 100px;
      }
      to {
        opacity: 0;
        transform: translateX(-10px);
        max-width: 0;
      }
    }

    .animate-slide-in {
      animation: slideIn 0.3s ease-out forwards;
    }

    .animate-slide-out {
      animation: slideOut 0.3s ease-in forwards;
    }
  `]
})
export class ShareInviteComponent {
  sessionId = input.required<string>();

  readonly copyState = signal<CopyState>('idle');
  readonly showCopied = computed(() => this.copyState() !== 'idle');

  copySessionUrl(): void {
    const url = `${window.location.origin}/${this.sessionId()}`;
    navigator.clipboard.writeText(url).then(() => {
      this.copyState.set('copied');

      setTimeout(() => {
        this.copyState.set('sliding-out');
        setTimeout(() => {
          this.copyState.set('idle');
        }, 300);
      }, 2000);
    });
  }
}
