import {afterNextRender, Component, computed, effect, ElementRef, inject, Injector, input, output, signal, viewChild} from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-inline-username',
  imports: [FormsModule],
  template: `
    @if (editable() && editing()) {
      <label for="inline-username" class="sr-only">Username</label>
      <input type="text" name="username" id="inline-username"
             [maxLength]="16"
             [(ngModel)]="localUsername"
             (blur)="save()"
             (keydown.enter)="usernameInput.blur()"
             (keydown.escape)="usernameInput.blur()"
             autocomplete="off"
             data-lpignore="true"
             data-1p-ignore
             class="font-medium text-sm bg-transparent border-b border-brand-500 focus:outline-none focus:border-brand-600 py-0 px-0 w-24 text-brand-500 dark:text-brand-400"
             #usernameInput/>
    } @else {
      <span class="font-medium text-sm truncate" [class]="nameClasses()">
        {{ username() }}
      </span>
      @if (editable()) {
        <button (click)="startEditing()"
                class="p-1 rounded hover:bg-surface-200 dark:hover:bg-gray-700 text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 transition-colors cursor-pointer"
                title="Edit username">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-3.5">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L6.832 19.82a4.5 4.5 0 0 1-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 0 1 1.13-1.897L16.863 4.487Zm0 0L19.5 7.125"/>
          </svg>
        </button>
      }
    }
  `,
  styles: [`
    :host {
      display: contents
    }
  `]
})
export class InlineUsernameComponent {

  private readonly injector = inject(Injector);

  username = input.required<string>();
  editable = input(false);
  highlighted = input(false);

  setUsername = output<string>();

  usernameInput = viewChild<ElementRef<HTMLInputElement>>('usernameInput');

  editing = signal(false);
  localUsername = signal<string | undefined>(undefined);

  nameClasses = computed(() => {
    if (this.highlighted()) {
      return 'text-brand-500 dark:text-brand-400';
    }
    return 'text-gray-600 dark:text-gray-400';
  });

  constructor() {
    effect(() => {
      this.localUsername.set(this.username());
    });
  }

  startEditing(): void {
    this.editing.set(true);
    afterNextRender(() => {
      this.usernameInput()?.nativeElement.focus();
      this.usernameInput()?.nativeElement.select();
    }, {injector: this.injector});
  }

  save(): void {
    this.editing.set(false);
    const newUsername = this.localUsername()?.trim();
    if (newUsername && newUsername !== this.username()) {
      localStorage.setItem("username", newUsername);
      this.setUsername.emit(newUsername);
    } else {
      this.localUsername.set(this.username());
    }
  }
}
