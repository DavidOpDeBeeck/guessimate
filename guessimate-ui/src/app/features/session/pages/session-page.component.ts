import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterOutlet} from '@angular/router';
import {filter, map} from 'rxjs';
import {EstimationLifecycleComponent} from '../../estimation/components/estimation-lifecycle.component';
import {WebSocketOverlayComponent, WebSocketStatusIndicatorComponent} from '../../../websocket';
import {EstimationCardsComponent} from '../../estimation/components/estimation-cards.component';
import {NavigationComponent} from '../../../layout/navigation/navigation.component';
import {SessionStore} from '../services/session.store';

@Component({
  selector: 'app-session-page',
  imports: [
    NavigationComponent,
    RouterOutlet,
    EstimationLifecycleComponent,
    WebSocketOverlayComponent,
    WebSocketStatusIndicatorComponent,
    EstimationCardsComponent
  ],
  providers: [SessionStore],
  template: `
    <div class="w-full flex flex-col items-center">
      <div class="w-full max-w-6xl flex flex-col pt-4 pb-2">
        <app-navigation/>

        @if (store.connection()) {
          <app-web-socket-overlay [connection]="store.connection()">
            <div class="flex flex-col-reverse md:flex-row items-start justify-center gap-4 px-4">
              @if (store.lobby()) {
                <div class="w-full flex-1 flex flex-col">
                  <router-outlet/>
                  <div class="flex justify-between items-center mt-4">
                    <span class="text-sm font-normal text-gray-600 dark:text-gray-400 italic">Session ID: {{ store.lobby()!.sessionId }}</span>
                    <app-web-socket-status-indicator [connection]="store.connection()"/>
                  </div>
                </div>
                <div class="w-full md:w-88 flex flex-col gap-4">
                  <app-estimation-lifecycle
                    [lobby]="store.lobby()!"
                    (startEstimation)="store.startEstimation()"
                    (revealEstimation)="store.revealEstimation()"
                    (sendReaction)="store.sendReaction($event)"
                  />
                  <app-estimation-cards
                    [lobby]="store.lobby()!"
                    (setEstimate)="store.setEstimate($event)"
                    (clearEstimate)="store.clearEstimate()"
                  />
                </div>
              }
            </div>
          </app-web-socket-overlay>
        }
      </div>
    </div>
  `
})
export class SessionPageComponent implements OnInit, OnDestroy {

  readonly store = inject(SessionStore);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.route.paramMap.pipe(
      map(params => params.get('id')),
      filter(sessionId => sessionId !== null)
    ).subscribe({
      next: (sessionId) => {
        this.store.initialize(sessionId);
      },
      error: () => this.router.navigate(["/"])
    });
  }

  ngOnDestroy(): void {
    this.store.destroy();
  }
}
