import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.scss']
})
export class ConfigComponent implements OnInit {

  databases = [];
  config = {};

  form = new FormGroup({
    databaseType: new FormControl(''),
    companyDomain: new FormControl(''),
    projectName: new FormControl(''),
    moduleName: new FormControl(''),
    keyword: new FormControl(''),
    keywordDesc: new FormControl(''),
    tableName: new FormControl(''),
    sequenceName: new FormControl(''),
    modelClazz: new FormControl(''),
    daoClazz: new FormControl(''),
    daoMapper: new FormControl(''),
    serviceInterface: new FormControl(''),
    serviceClazz: new FormControl(''),
    controllerClazz: new FormControl(''),
    jsonControllerClazz: new FormControl('')
  });

  constructor(private configService: ConfigService, private router: Router) {
    this.databases = configService.getDatabases();
    this.config = configService.getConfig();
  }

  ngOnInit() {
    this.form.get('keyword').valueChanges.subscribe(this.onKeywordChange);
    this.form.setValue(this.config);
  }

  onKeywordChange = (keyword: string) => {
    if (!keyword) {
      return;
    }
    let keywordClazz = keyword;
    let keywordResource = keyword;
    let keywordTable = keyword;

    if (keyword.indexOf('.') > 0) {
      const index = keyword.lastIndexOf('.');
      let lastIndex = index + 1;
      if (lastIndex > keyword.length) {
        lastIndex = index;
      }
      keywordClazz = keyword.substring(0, index).toLowerCase() + '.' + keyword.substring(lastIndex);
      keywordResource = keywordClazz.replace('.', '/');
      keywordTable = keywordClazz.replace('.', '_');
    }
    keywordTable = keywordTable.toLowerCase();

    const companyDomain = this.form.get('companyDomain').value || `com.company`;
    const projectName = this.form.get('projectName').value || `proj`;
    const moduleName = this.form.get('moduleName').value || `module`;

    const keywordDesc = `${keywordClazz} Description`;
    const tableName = `t_${keywordTable}`;
    const sequenceName = `t_${keywordTable}_seq`;
    const modelClazz = `${companyDomain}.${projectName}.model.${moduleName}.${keywordClazz}Vo`;
    const daoClazz = `${companyDomain}.${projectName}.dao.inf.${moduleName}.${keywordClazz}Mapper`;
    const daoMapper = `resources/mappings/${projectName}/${moduleName}/${keywordResource}Mapper.xml`;
    const serviceInterface = `${companyDomain}.${projectName}.bs.inf.${moduleName}.${keywordClazz}Service`;
    const serviceClazz = `${companyDomain}.${projectName}.bs.impl.${moduleName}.${keywordClazz}ServiceImpl`;
    const controllerClazz = `${companyDomain}.${projectName}.controller.url.${moduleName}.${keywordClazz}UrlController`;
    const jsonControllerClazz = `${companyDomain}.${projectName}.controller.json.${moduleName}.${keywordClazz}JsonController`;

    this.form.get('tableName').patchValue(tableName);
    this.form.get('sequenceName').patchValue(sequenceName);
    this.form.get('modelClazz').patchValue(modelClazz);
    this.form.get('daoClazz').patchValue(daoClazz);
    this.form.get('daoMapper').patchValue(daoMapper);
    this.form.get('serviceInterface').patchValue(serviceInterface);
    this.form.get('serviceClazz').patchValue(serviceClazz);
    this.form.get('controllerClazz').patchValue(controllerClazz);
    this.form.get('jsonControllerClazz').patchValue(jsonControllerClazz);
  }

  onNext() {
    this.configService.setConfig(this.form.value);
    this.router.navigate(['property']);
  }
}
