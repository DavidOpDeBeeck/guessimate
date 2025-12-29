import {Component, computed, effect, input, output, signal} from '@angular/core';
import {LobbyInfo, UserInfo} from '../../session/models/session.model';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-username-selector',
  imports: [
    FormsModule
  ],
  standalone: true,
  template: `
    <div class="flex flex-col gap-4 py-3 px-4">
      <div class="flex gap-3 items-center">
        <div class="p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-900 dark:text-white">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"/>
          </svg>
        </div>
        <div class="flex flex-col">
          <span class="text-sm font-medium text-gray-900 dark:text-white">Username</span>
          <span class="text-xs text-gray-500 dark:text-gray-400">Visible to others</span>
        </div>
      </div>
      <div>
        <label for="displayName" class="sr-only">Display Name</label>
        <input type="text" name="displayName" id="displayName"
               [maxLength]="16"
               [(ngModel)]="username"
               (blur)="save()"
               (keydown.enter)="onEnter($event)"
               autocomplete="off"
               data-lpignore="true"
               data-1p-ignore
               class="bg-surface-200 border-none text-gray-900 text-sm rounded-md focus:ring-2 focus:ring-brand-500 block w-full py-2 px-3 dark:bg-gray-800 dark:text-white dark:placeholder-gray-400 dark:focus:ring-brand-500"
               placeholder="Type username" required="true">
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: contents
    }
  `]
})
export class UsernameSelectorComponent {
  lobby = input.required<LobbyInfo>();
  setUsername = output<string>();

  currentUser = computed<UserInfo | null>(() => (this.lobby()?.users || []).find(user => user.self) ?? null);
  username = signal<string | undefined>(undefined);
  isChanged = computed(() => this.username() !== this.currentUser()?.username && this.username() !== '');

  constructor() {
    effect(() => {
      this.username.set(this.currentUser()?.username);
    });
  }

  onEnter(event: Event) {
    (event.target as HTMLInputElement).blur();
  }

  save() {
    const newUsername = this.username();
    if (newUsername && this.isChanged()) {
      localStorage.setItem("username", newUsername);
      this.setUsername.emit(newUsername);
    }
  }
}

