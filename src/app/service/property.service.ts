import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
    providedIn: 'root'
})
export class PropertyService {

    defaultRowData = [{
        accessModifier: '',
        propertyType: '',
        propertyName: '',
        propertyDesc: '',
        propertyExport: '',
        columnName: '',
        columnType: '',
        columnLength: '',
        columnPrecision: '',
        columnPrimaryKey: '',
        columnIdentity: ''
    }];
    rowData = [];
    columnTypeMap = new Map();

    constructor(private cookieService: CookieService) {
        const oracleMap = new Map();
        this.columnTypeMap.set('Oracle', oracleMap);
        oracleMap.set('String', { columnType: 'VARCHAR2', columnLength: 255, columnPrecision: null });
        oracleMap.set('BigDecimal', { columnType: 'NUMBER', columnLength: null, columnPrecision: null });
        oracleMap.set('Boolean', { columnType: 'NUMBER', columnLength: 1, columnPrecision: null });
        oracleMap.set('Byte', { columnType: 'NUMBER', columnLength: 3, columnPrecision: null });
        oracleMap.set('Short', { columnType: 'NUMBER', columnLength: 5, columnPrecision: null });
        oracleMap.set('Integer', { columnType: 'NUMBER', columnLength: 10, columnPrecision: null });
        oracleMap.set('Long', { columnType: 'NUMBER', columnLength: 20, columnPrecision: null });
        oracleMap.set('Float', { columnType: 'NUMBER', columnLength: null, columnPrecision: null });
        oracleMap.set('Double', { columnType: 'NUMBER', columnLength: null, columnPrecision: null });
        oracleMap.set('Date', { columnType: 'TIMESTAMP', columnLength: null, columnPrecision: null });
        oracleMap.set('Timestamp', { columnType: 'TIMESTAMP', columnLength: null, columnPrecision: null });
    }

    getRowData() {
        const properties = this.cookieService.get('properties');
        if (properties) {
            return JSON.parse(properties);
        } else if (this.rowData && this.rowData.length > 0) {
            return this.rowData;
        } else {
            return this.getDefaultRowData();
        }
    }

    setRowData(rowData: any) {
        this.rowData = rowData;
        this.cookieService.set('properties', JSON.stringify(rowData));
    }

    getColumnType(databaseType: string, propertyType: string) {
        if (this.columnTypeMap.has(databaseType)) {
            return this.columnTypeMap.get(databaseType).get(propertyType);
        } else {
            return { columnType: 'VARCHAR', columnLength: 255, columnPrecision: null };
        }
    }

    getDefaultRowData() {
        return JSON.parse(JSON.stringify(this.defaultRowData));
    }
}
