import {Component, Inject, OnInit, ViewEncapsulation} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-instruction-dialog',
  templateUrl: './instruction-dialog.component.html',
  styleUrls: ['./instruction-dialog.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class InstructionDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<InstructionDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {

  }

  ngOnInit(): void {

  }

}
