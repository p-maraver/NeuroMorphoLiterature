import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-acknowledgment',
  templateUrl: './acknowledgment.component.html',
  styleUrls: ['./acknowledgment.component.css']
})
export class AcknowledgmentComponent implements OnInit {

  @Input() acknowledgment: string;

  constructor() { }

  ngOnInit() {
  }

}
