import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {RequestService} from '../request.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-cadtipoeventos',
  templateUrl: './cadtipoeventos.component.html',
  styleUrls: ['./cadtipoeventos.component.css']
})
export class CadtipoeventosComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CadtipoeventosComponent>,
  	@Inject(MAT_DIALOG_DATA) public data: any,
  	private request:RequestService,
  	private snackBar:MatSnackBar
  	) { }

  ngOnInit() {	
	console.log(this.data);
  }

  public doSave() {
  	this.request.post("tipoevento", this.data).subscribe(ret => {

  		let evento:any = (ret as any).evento;
  		this.data.id = evento.id;

  		this.dialogRef.close();

  	}, error => {
  		this.snackBar.open(error.message,"Não foi possível remover", {
		      duration: 5000,
		    });	
  	});
  }

}
