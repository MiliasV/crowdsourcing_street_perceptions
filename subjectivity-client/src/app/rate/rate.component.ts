import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Rate} from "./rate";
import {animate, query, stagger, style, transition, trigger} from "@angular/animations";
import {conditionallyCreateMapObjectLiteral} from "@angular/compiler/src/render3/view/util";


const listAnimation = trigger('fadeSlideInOut', [
  transition(':enter', [
    style({ opacity: 0, transform: 'translateY(10px)' }),
    animate('800ms', style({ opacity: 1, transform: 'translateY(0)' })),
  ]),
  transition(':leave', [
    animate('500ms', style({ opacity: 0, transform: 'translateY(10px)' })),
  ]),
]);


@Component({
  selector: 'app-rate',
  templateUrl: './rate.component.html',
  styleUrls: ['./rate.component.scss'],
  animations: [listAnimation]
})
export class RateComponent implements OnInit {

  @Input() rate: Rate = {};
  @Input() defaultRate: Rate = {};
  @Input() isSimilarity: boolean;
  @Output() rateChange = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
    if (this.defaultRate) {
      this.rate.safety = this.defaultRate.safety;
      this.rate.attractiveness = this.defaultRate.attractiveness;
      this.rate.business = this.defaultRate.business;
    }
  }

  onKeepScoreChange(flag: any) {
    if (flag && flag.value) {
      if (this.rate && this.defaultRate) {
        this.rate.safety = this.defaultRate.safety;
        this.rate.attractiveness = this.defaultRate.attractiveness;
        this.rate.business = this.defaultRate.business;
      }
    } else {
      if (this.rate) {
        this.rate.safety = null;
        this.rate.attractiveness = null;
        this.rate.business = null;
      }
    }
    this.onRateChange();
  }

  onRateChange(){
    this.rateChange.next(this.rate);
  }
}
