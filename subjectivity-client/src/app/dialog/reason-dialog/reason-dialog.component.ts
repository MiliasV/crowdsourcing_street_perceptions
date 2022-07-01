import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Rate} from "../../rate/rate";

@Component({
  selector: 'app-reason-dialog',
  templateUrl: './reason-dialog.component.html',
  styleUrls: ['./reason-dialog.component.scss']
})
export class ReasonDialogComponent implements OnInit {

  rate: Rate = {};
  continueActive: boolean = false;

  constructor(public dialogRef: MatDialogRef<ReasonDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.rate = this.data.rate;
    this.reasonChange();
  }

  reasonChange() {
    if (this.data.isEven || (this.rate.attractiveness == 1 || this.rate.attractiveness == 5)) {
      if (this.rate && this.rate.attractivenessReason && this.rate.attractivenessReason.length > 2)
        this.continueActive = true;
      else
        this.continueActive = false;
    }else
      this.continueActive = true;

    if (this.continueActive == true) {
      if (this.data.isEven || (this.rate.safety == 1 || this.rate.safety == 5)) {
        if (this.rate && this.rate.safetyReason && this.rate.safetyReason.length > 2)
          this.continueActive = true;
        else
          this.continueActive = false;
      } else
        this.continueActive = true;
    }
  }
}
