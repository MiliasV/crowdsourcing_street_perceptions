import {Component, Inject, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Rate} from "../../rate/rate";
import {LinkPipe} from "../../utils/link.pipe";

@Component({
  selector: 'app-similarity-dialog',
  templateUrl: './similarity-dialog.component.html',
  styleUrls: ['./similarity-dialog.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SimilarityDialogComponent implements OnInit {

  rate: Rate = {};
  innerHeight: number;
  currentImage: any = null;
  similarImage: any = null;
  submitActive: boolean = false;

  constructor(public dialogRef: MatDialogRef<SimilarityDialogComponent>, public linkPipe: LinkPipe,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.innerHeight = window.innerHeight;
  }

  ngOnInit(): void {
    const reader = new FileReader();
    reader.onloadend = () => {
      this.currentImage = reader.result;
    };
    reader.readAsDataURL(this.data.current);
    this.similarImage = this.linkPipe.transform(this.data.image , 'image');
  }

  autoScroll(){
    setTimeout(() => {
      let elem = document.getElementById('dialogContent');
      elem.scrollTop = elem.scrollHeight;
    }, 500);
  }

  onRateChange(rate: Rate) {
    this.rate = Object.assign({}, rate);
    if (this.rate.keepScores != null)
      this.autoScroll();
    if (this.rate && this.rate.similarity) {
      if (this.rate.keepScores != null) {
        if (this.rate.attractiveness != null && this.rate.safety != null && this.rate.business != null)
          this.submitActive = true;
        else
          this.submitActive = false;
      } else
        this.submitActive = false;
    } else
      this.submitActive = false;
  }

}
