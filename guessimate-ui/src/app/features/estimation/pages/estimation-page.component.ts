import {Component, inject} from '@angular/core';
import {EstimatorOverviewComponent} from '../components/estimators/estimator-overview.component';
import {ObserverOverviewComponent} from '../components/observers/observer-overview.component';
import {SessionStore} from '../../session/services/session.store';

@Component({
  selector: 'app-estimation-page',
  imports: [
    EstimatorOverviewComponent,
    ObserverOverviewComponent,
  ],
  standalone: true,
  template: `
    <div class="flex-1 flex flex-col gap-4">
      <app-estimator-overview [lobby]="store.lobby()!" (setUserRole)="store.setUserRole($event)"/>
      <app-observer-overview [lobby]="store.lobby()!" (setUserRole)="store.setUserRole($event)"/>
    </div>
  `
})
export class EstimationPageComponent {
  readonly store = inject(SessionStore);
}
