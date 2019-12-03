import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

import {LicenseManager} from '@ag-grid-enterprise/core';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));


LicenseManager.setLicenseKey('33084345600000_MzMwODQzNDU2MDAwMDA=87fb229b2acf288aaa8fb05939f373e1');
