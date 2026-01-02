import {Component, inject} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {Session} from '../../session/models/session.model';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    FormsModule
  ],
  template: `
    <div class="w-full h-full flex flex-col justify-center items-center p-4">
      <div class="flex flex-col items-center">
        <h1 class="mb-4 text-6xl font-extrabold leading-none tracking-tight text-gray-900 lg:text-8xl dark:text-white">Guessimate</h1>
        <p class="mb-6 text-lg font-normal text-gray-600 lg:text-2xl sm:px-16 xl:px-48 dark:text-gray-400">Estimate together. Decide smarter.</p>
        <button (click)="createSession()"
                class="inline-flex items-center justify-center px-5 py-3 text-base font-medium text-center text-white rounded-lg bg-brand-600 hover:bg-brand-700 focus-visible:outline-none focus-visible:ring-4 focus-visible:ring-brand-200 dark:bg-brand-600 dark:hover:bg-brand-700 dark:focus-visible:ring-brand-800 cursor-pointer">
          Start Estimating
          <svg class="w-3.5 h-3.5 ms-2 rtl:rotate-180" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 10">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M1 5h12m0 0L9 1m4 4L9 9"/>
          </svg>
        </button>
      </div>
    </div>
  `
})
export class HomePageComponent {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  createSession() {
    this.http.post<Session>(`/api/sessions`, {})
      .subscribe((session) => this.router.navigate(['/', session.sessionId]));
  }
}
