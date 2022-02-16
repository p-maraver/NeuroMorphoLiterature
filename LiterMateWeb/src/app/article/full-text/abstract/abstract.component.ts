import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-abstract',
  templateUrl: './abstract.component.html',
  styleUrls: ['./abstract.component.css']
})
export class AbstractComponent implements OnInit {
  @Input() abstractContent: string;

  constructor() { }

  ngOnInit() {
  }

}
