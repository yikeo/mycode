import { Component, OnInit, ViewChild } from '@angular/core';
import { PropertyService } from 'src/app/service/property.service';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router';
import { AllModules } from '@ag-grid-enterprise/all-modules';

@Component({
  selector: 'app-property',
  templateUrl: './property.component.html',
  styleUrls: ['./property.component.scss']
})
export class PropertyComponent implements OnInit {

  private gridApi: any;
  private gridColumnApi: any;

  defaultColDef = {
    resizable: true,
    width: 100,
    editable: true
  };

  columnDefs = [
    {
      headerName: '访问修饰', field: 'accessModifier', checkboxSelection: true, width: 120,
      cellEditor: 'agRichSelectCellEditor',
      cellEditorParams: {
        values: ['', 'public', 'protected', 'private']
      }
    },
    {
      headerName: '属性类型', field: 'propertyType',
      cellEditor: 'agRichSelectCellEditor',
      cellEditorParams: {
        values: ['String', 'Boolean', 'Byte', 'Short', 'Integer', 'Long', 'Float', 'Double', 'BigDecimal', 'Date', 'Timestamp']
      }
    },
    { headerName: '属性名', field: 'propertyName' },
    { headerName: '属性描述', field: 'propertyDesc' },
    { headerName: '导出顺序', field: 'propertyExport' },
    { headerName: '字段名', field: 'columnName' },
    {
      headerName: '字段类型', field: 'columnType',
      cellEditor: 'agRichSelectCellEditor',
      cellEditorParams: {
        values: ['VARCHAR2', 'NUMBER', 'TIMESTAMP']
      }
    },
    { headerName: '字段长度', field: 'columnLength' },
    { headerName: '字段精度', field: 'columnPrecision'},
    { headerName: '主键', field: 'columnPrimaryKey', width: 75 },
    { headerName: '唯一', field: 'columnIdentity', width: 75 }
  ];

  rowData = [];

  modules = AllModules;

  constructor(private configService: ConfigService,
              private propertyService: PropertyService,
              private router: Router) {
    this.rowData.push(...propertyService.getRowData());
  }

  ngOnInit() {
  }

  onCellValueChanged(params: any) {
    const colId = params.column.getId();
    if (colId === 'propertyType') {
      const data = params.data;
      const propertyType = data.propertyType;
      const databaseType = this.configService.getConfig().databaseType;
      const columnTypeMap = this.propertyService.getColumnType(databaseType, propertyType);
      params.node.setDataValue('columnType', columnTypeMap.columnType);
      params.node.setDataValue('columnLength', columnTypeMap.columnLength);
      params.node.setDataValue('columnPrecision', columnTypeMap.columnPrecision);
    }
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
  }

  addRow(params: any) {
    if (this.gridApi) {
      const defaultData = this.propertyService.getDefaultRowData();
      const transaction = {
        add: defaultData
      };
      this.gridApi.updateRowData(transaction);
    }
  }

  deleteRow(params: any) {
    if (this.gridApi) {
      const selectedData = this.gridApi.getSelectedRows();
      this.gridApi.updateRowData({remove: selectedData});
    }
  }

  onNext() {
    var gridData = [];
    if (this.gridApi) {
      this.gridApi.forEachNode((node: any, index: any) => {
        gridData.push(node.data);
      });
    }
    this.propertyService.setRowData(gridData);
    this.router.navigate(['query']);
  }

}
