import {Component, computed, input, output} from '@angular/core';
import {Emoji, LobbyInfo} from '../../session/models/session.model';

@Component({
  selector: 'app-reaction-counter',
  standalone: true,
  template: `
    @if (lobby().configuration.reactionsEnabled) {
      <div class="flex flex-wrap items-center justify-center gap-1.5 pt-4 select-none">
        @for (emoji of emojis; track emoji.type) {
          <button (click)="sendReaction.emit(emoji.type)"
                  type="button"
                  [title]="emoji.label"
                  [disabled]="!hasRole()"
                  class="relative inline-flex items-center gap-1 px-2 py-0.5 text-sm font-medium rounded-lg border focus:z-10 focus:ring-4 text-gray-900 bg-gray-50/50 border-gray-100 hover:bg-gray-50 hover:text-brand-700 focus:ring-gray-100 dark:focus:ring-gray-800 dark:bg-gray-900/40 dark:text-gray-400 dark:border-gray-800/60 dark:hover:text-white dark:hover:bg-gray-800/60 cursor-pointer transition-colors disabled:text-gray-400 disabled:bg-gray-50 disabled:border-gray-100 disabled:cursor-not-allowed disabled:dark:bg-gray-800 disabled:dark:text-gray-600 disabled:dark:border-gray-700">
            <div class="relative">
              <span>{{ emoji.icon }}</span>
            </div>
            @if (reactionCounts()[emoji.type] > 0) {
              <span class="text-xs font-semibold text-gray-900 dark:text-white">
                {{ reactionCounts()[emoji.type] }}
              </span>
            }
          </button>
        }
      </div>
    }
  `
})
export class ReactionCounterComponent {

  lobby = input.required<LobbyInfo>();
  sendReaction = output<Emoji>();

  emojis = [
    {type: 'THUMBS_UP' as Emoji, icon: '👍', label: 'Thumbs Up'},
    {type: 'CONFETTI' as Emoji, icon: '🎉', label: 'Confetti'},
    {type: 'HEART' as Emoji, icon: '❤️', label: 'Heart'},
    {type: 'FIRE' as Emoji, icon: '🔥', label: 'Fire'},
    {type: 'LIGHTNING' as Emoji, icon: '⚡', label: 'Lightning'}
  ];

  hasRole = computed(() => this.lobby().users.find(user => user.self)?.role !== null);

  reactionCounts = computed(() => {
    const counts: Record<Emoji, number> = {
      'THUMBS_UP': 0,
      'CONFETTI': 0,
      'HEART': 0,
      'FIRE': 0,
      'LIGHTNING': 0
    };
    for (const user of this.lobby().users) {
      if (user.reaction) {
        counts[user.reaction]++;
      }
    }
    return counts;
  });
}