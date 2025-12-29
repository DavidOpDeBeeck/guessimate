import {Component, signal} from "@angular/core";

@Component({
  selector: 'app-copy-url-button',
  template: `
    <button (click)="copyToClipboard()"
            type="button"
            [class]="copied()
              ? 'w-[85px] flex items-center justify-center gap-2 px-3 py-2 text-sm font-medium text-success-600 dark:text-success-400 bg-success-50 dark:bg-success-900/30 rounded-md transition-colors cursor-pointer'
              : 'w-[85px] flex items-center justify-center gap-2 px-3 py-2 text-sm font-medium text-gray-600 rounded-md hover:bg-surface-200 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-800 dark:hover:text-white transition-colors cursor-pointer'">
      @if (copied()) {
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="size-4 shrink-0">
          <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5"/>
        </svg>
        <span>Copied</span>
      } @else {
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4 shrink-0">
          <path stroke-linecap="round" stroke-linejoin="round"
                d="M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244"/>
        </svg>
        <span>Invite</span>
      }
    </button>
  `
})
export class CopyUrlButtonComponent {

  copied = signal(false);

  copyToClipboard() {
    navigator.clipboard.writeText(window.location.origin + '/' + window.location.pathname.split('/')[1]).then(() => {
      this.copied.set(true);
      setTimeout(() => this.copied.set(false), 2000);
    });
  }
}
