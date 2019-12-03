import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
    providedIn: 'root'
})
export class ConfigService {

    databases = [
        'Oracle',
        'MySQL',
        'SQLServer',
        'DB2',
        'Sybase',
        'PostgreSQL',
    ];
    config: any;
    defaultConfig = {
        databaseType: 'Oracle',
        companyDomain: 'com.thunis',
        projectName: 'ba',
        moduleName: 'bigorder',
        keyword: '',
        keywordDesc: '',
        tableName: '',
        sequenceName: '',
        modelClazz: '',
        daoClazz: '',
        daoMapper: '',
        serviceInterface: '',
        serviceClazz: '',
        controllerClazz: '',
        jsonControllerClazz: ''
    };

    constructor(private cookieService: CookieService) { }

    getDatabases() {
        return this.databases;
    }

    getConfig() {
        const config = this.cookieService.get('config');
        if (config) {
            return JSON.parse(config);
        } else {
            return this.config || this.defaultConfig;
        }
    }

    setConfig(config: any) {
        this.config = config;
        this.cookieService.set('config', JSON.stringify(config));
    }
}
