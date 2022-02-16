import {Component, Input, OnInit} from '@angular/core';
import {Bibliography} from '../model/bibliography';

@Component({
  selector: 'app-references',
  templateUrl: './references.component.html',
  styleUrls: ['./references.component.css']
})
export class ReferencesComponent implements OnInit {

  @Input() referenceList: Bibliography[];

  constructor() { }

  ngOnInit() {
  }

}
