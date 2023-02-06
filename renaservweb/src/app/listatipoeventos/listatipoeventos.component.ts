import { Component, OnInit, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {RequestService} from '../request.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {CadtipoeventosComponent} from '../cadtipoeventos/cadtipoeventos.component';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';


@Component({
  selector: 'app-listatipoeventos',
  templateUrl: './listatipoeventos.component.html',
  styleUrls: ['./listatipoeventos.component.css']
})
export class ListatipoeventosComponent implements OnInit {

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  filtro:any = {argument: ''};
    
  eventos = [];
  displayedColumns: string[] = ['id', 'descricao'];
  dataSource:MatTableDataSource<any>;

  constructor(private request:RequestService, private snackBar:MatSnackBar, public dialog: MatDialog) { }

  ngOnInit() {
  	let f:any = this.request.getFilter("listaveiculos");
    if (f) {
      this.filtro = f;
      this.paginator.pageIndex = f.page;
      this.paginator.pageSize = f.limit;
      
    }
  	this.dataSource = new MatTableDataSource(this.eventos);
    this.dataSource.paginator = this.paginator;

    this.loadData();

  }

  public loadData():void {
  	this.request.get('listatipoeventos','').subscribe(ret => {
  		let retorno = ret as any;

  		if (retorno.status == 'OK') {
  			this.eventos = retorno.eventos;
  			this.dataSource.data = this.eventos;
  		}
  	});
  }

  public doDelete(evento:any):void{
  	if (window.confirm('Deseja remover este evento?')) {
	  	this.request.delete('tipoevento','&id=' + evento.id).subscribe(ret => {
	  		let retorno = ret as any;

	  		if (retorno.status == 'OK') {
	  			this.loadData();
	  		} else {
	  			this.snackBar.open(retorno.message,"Não foi possível remover", {
			      duration: 5000,
			    });	
	  		}
	  	},
	  	error => {
	  		console.log(error);

	  		this.snackBar.open(error.message,"Não foi possível remover", {
		      duration: 5000,
		    });	
	  	});	
	  }
  }

  public doOpen(tipoEvento) {
     const dialogRef = this.dialog.open(CadtipoeventosComponent, {
      width: '250px',
        data: tipoEvento
      });

      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        //this.animal = result;
      });
  }

  public doAdd() {
     let evento = {id: 0, descricao: 'Novo'};
     this.eventos.push(evento);

     const dialogRef = this.dialog.open(CadtipoeventosComponent, {
      width: '250px',
        data: evento
      });

      dialogRef.afterClosed().subscribe(result => {
          this.loadData();
      });
  }

}
