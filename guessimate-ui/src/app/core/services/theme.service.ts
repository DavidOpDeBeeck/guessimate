import {Injectable, signal} from '@angular/core';

export type ThemePreference = 'light' | 'dark' | 'system';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private readonly STORAGE_KEY = 'theme';
  readonly theme = signal<ThemePreference>('dark');

  constructor() {
    const stored = localStorage.getItem(this.STORAGE_KEY) as ThemePreference | null;
    if (stored && ['light', 'dark', 'system'].includes(stored)) {
      this.theme.set(stored);
    }
    this.applyTheme();
    this.listenToSystemChanges();
  }

  setTheme(preference: ThemePreference) {
    this.theme.set(preference);
    localStorage.setItem(this.STORAGE_KEY, preference);
    this.applyTheme();
  }

  private applyTheme() {
    const html = document.documentElement;
    const preference = this.theme();

    if (preference === 'system') {
      const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      html.classList.toggle('dark', isDark);
    } else {
      html.classList.toggle('dark', preference === 'dark');
    }
  }

  private listenToSystemChanges() {
    window.matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', () => {
        if (this.theme() === 'system') {
          this.applyTheme();
        }
      });
  }
}
