import {Component, Input, OnInit} from '@angular/core';
import {SupplementaryMaterial} from '../model/supplementary-material';

@Component({
  selector: 'app-supplementary-material',
  templateUrl: './supplementary-material.component.html',
  styleUrls: ['./supplementary-material.component.css']
})
export class SupplementaryMaterialComponent implements OnInit {

  @Input() supplementaryMaterial: SupplementaryMaterial;

  constructor() { }

  ngOnInit() {
  }

}
