import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app-routing/app-routing.module';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { provideAnimations } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAnimationsAsync(), importProvidersFrom(CalendarModule.forRoot({ provide: DateAdapter, useFactory: adapterFactory })), provideAnimations()]
};
