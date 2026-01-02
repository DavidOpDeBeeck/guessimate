import {Component, computed, input, output} from '@angular/core';
import {UserInfo} from '../../../session/models/session.model';
import {InlineUsernameComponent} from '../inline-username.component';

@Component({
  selector: 'app-observer-detail',
  imports: [InlineUsernameComponent],
  template: `
    <div class="flex items-center justify-between gap-4 py-3 px-4">
      <div class="flex items-center gap-3 min-w-0">
        <div [class]="iconContainerClasses()" class="shrink-0 flex items-center justify-center size-8 rounded-full">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"/>
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"/>
          </svg>
        </div>
        <app-inline-username
          [username]="user().username"
          [editable]="user().self"
          [highlighted]="user().self"
          (setUsername)="setUsername.emit($event)"
        />
      </div>
    </div>
  `
})
export class ObserverDetailComponent {

  user = input.required<UserInfo>();
  setUsername = output<string>();

  iconContainerClasses = computed(() => {
    if (this.user().self) {
      return 'bg-brand-100 text-brand-600 dark:bg-brand-900/30 dark:text-brand-400';
    }
    return 'bg-gray-100 text-gray-500 dark:bg-gray-800 dark:text-gray-400';
  });
}
