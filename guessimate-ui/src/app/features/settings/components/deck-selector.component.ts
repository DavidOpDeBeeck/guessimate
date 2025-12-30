import {Component, computed, inject, input, output} from '@angular/core';
import {Deck, LobbyInfo} from '../../session/models/session.model';
import {HttpClient} from '@angular/common/http';
import {toSignal} from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-deck-selector',
  imports: [],
  template: `
    <div class="flex flex-col gap-4 py-3 px-4">
      <div class="flex gap-3 items-center">
        <div class="p-2 bg-surface-200 dark:bg-gray-800 rounded-lg shrink-0">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5 text-gray-900 dark:text-white">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M3.75 6A2.25 2.25 0 0 1 6 3.75h2.25A2.25 2.25 0 0 1 10.5 6v2.25a2.25 2.25 0 0 1-2.25 2.25H6a2.25 2.25 0 0 1-2.25-2.25V6ZM3.75 15.75A2.25 2.25 0 0 1 6 13.5h2.25a2.25 2.25 0 0 1 2.25 2.25V18a2.25 2.25 0 0 1-2.25 2.25H6A2.25 2.25 0 0 1 3.75 18v-2.25ZM13.5 6a2.25 2.25 0 0 1 2.25-2.25H18A2.25 2.25 0 0 1 20.25 6v2.25A2.25 2.25 0 0 1 18 10.5h-2.25a2.25 2.25 0 0 1-2.25-2.25V6ZM13.5 15.75a2.25 2.25 0 0 1 2.25-2.25H18a2.25 2.25 0 0 1 2.25 2.25V18A2.25 2.25 0 0 1 18 20.25h-2.25A2.25 2.25 0 0 1 13.5 18v-2.25Z"/>
          </svg>
        </div>
        <div class="flex flex-col">
          <span class="text-sm font-medium text-gray-900 dark:text-white">Deck</span>
          <span class="text-xs text-gray-500 dark:text-gray-400">Select estimation cards</span>
        </div>
      </div>
      <div class="grid grid-cols-2 gap-1 p-1 bg-surface-200 dark:bg-gray-800 rounded-md">
        @for (deck of decks(); track deck.name) {
          <button (click)="setDeck.emit(deck.name)"
                  [class]="deck.name === currentDeck().name
                    ? 'flex flex-col items-center gap-1.5 px-2 py-2 rounded bg-surface-50 dark:bg-gray-700 text-gray-900 dark:text-white shadow-sm cursor-pointer'
                    : 'flex flex-col items-center gap-1.5 px-2 py-2 rounded text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300 cursor-pointer'">
            <span class="text-xs font-semibold">{{ deck.name }}</span>
            <div class="flex gap-1 flex-wrap justify-center">
              @for (card of deck.cards; track card) {
                <span [class]="deck.name === currentDeck().name
                    ? 'px-1.5 py-0.5 text-[10px] font-medium rounded bg-surface-200 dark:bg-gray-600 text-gray-900 dark:text-white border border-surface-300 dark:border-gray-500'
                    : 'px-1.5 py-0.5 text-[10px] font-medium rounded bg-surface-50 dark:bg-gray-700 border border-surface-300 dark:border-gray-600'">
                    {{ card }}
                  </span>
              }
            </div>
          </button>
        }
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: contents
    }
  `]
})
export class DeckSelectorComponent {

  lobby = input.required<LobbyInfo>();
  setDeck = output<string>()

  http = inject(HttpClient);

  decks = toSignal(this.http.get<Deck[]>(`/api/decks`), {initialValue: []})
  currentDeck = computed(() => this.lobby().configuration.deck);
}
