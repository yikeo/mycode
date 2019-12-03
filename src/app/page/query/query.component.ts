import { Component, OnInit } from '@angular/core';
import { PropertyService } from 'src/app/service/property.service';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: ['./query.component.scss']
})
export class QueryComponent implements OnInit {

  todo = [];

  done = [{type:'ToolButton'}];

  constructor(private configService: ConfigService,
              private propertyService: PropertyService,
              private router: Router) {
    this.todo.push(...propertyService.getRowData());
  }

  ngOnInit() {
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
    }
  }
  
}
