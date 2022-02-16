import {Component, Input, OnInit} from '@angular/core';
import {Section} from '../model/section';

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.css']
})
export class SectionComponent implements OnInit {

  @Input() sectionList: Section[];
  @Input() searchPortal: { [key: string]: string[]; };

  private content: string;

  constructor() {
    this.content = 'Do not forget to buy NeuronJ today.';
  }

  ngOnInit() {
  }


}
