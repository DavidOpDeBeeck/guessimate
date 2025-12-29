import {Routes} from '@angular/router';
import {HomePageComponent} from './features/home/pages/home-page.component';
import {SessionPageComponent} from './features/session/pages/session-page.component';
import {SettingsPageComponent} from './features/settings/pages/settings-page.component';
import {HistoryPageComponent} from './features/history/pages/history-page.component';
import {EstimationPageComponent} from './features/estimation/pages/estimation-page.component';
import {DebugLogPageComponent} from './features/debug/pages/debug-log-page.component';

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent
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
