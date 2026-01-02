import {Routes} from '@angular/router';
import {HomePageComponent} from './features/home/pages/home-page.component';
import {SessionPageComponent} from './features/session/pages/session-page.component';
import {SettingsPageComponent} from './features/settings/pages/settings-page.component';
import {PreferencesPageComponent} from './features/settings/pages/preferences-page.component';
import {HistoryPageComponent} from './features/history/pages/history-page.component';
import {EstimationPageComponent} from './features/estimation/pages/estimation-page.component';
import {DebugLogPageComponent} from './features/debug/pages/debug-log-page.component';
import {MetricsPageComponent} from './features/metrics/pages/metrics-page.component';

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent
  },
  {
    path: 'metrics',
    component: MetricsPageComponent
  },
  {
    path: ':id',
    component: SessionPageComponent,
    children: [
      {
        path: '',
        component: EstimationPageComponent
      },
      {
        path: 'preferences',
        component: PreferencesPageComponent
      },
      {
        path: 'settings',
        component: SettingsPageComponent
      },
      {
        path: 'history',
        component: HistoryPageComponent
      },
      {
        path: 'debug',
        component: DebugLogPageComponent
      }
    ]
  },
];
