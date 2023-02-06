import { Component, OnInit, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {RequestService} from '../request.service';
import {Router} from '@angular/router';
import { CadusuarioComponent } from '../cadusuario/cadusuario.component';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-listausuario',
  templateUrl: './listausuario.component.html',
  styleUrls: ['./listausuario.component.css']
})
export class ListausuarioComponent implements OnInit {
  
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  displayedColumns: string[] = ['id', 'usuario', 'dataLogin', 'ip', 'tipo'];
  dataSource:MatTableDataSource<any>;
  filtro = {argument: ''};

  constructor(private request:RequestService, private router:Router, private dialog: MatDialog, private snackBar:MatSnackBar) { }

  ngOnInit() {
  	this.dataSource = new MatTableDataSource();
  	this.dataSource.paginator = this.paginator;
    this.loadData();
  }

  public loadData():void{
  	this.request.post('listausuarios', this.filtro).subscribe(resp => {
  		let retorno = resp as any;
  		if (retorno.status == 'OK') {
  			this.dataSource.data = retorno.usuarios;
  		}
  	});
  }

  public doOpen(usuario) {
     const dialogRef = this.dialog.open(CadusuarioComponent, {
      width: '400px',
        data: usuario
      });

      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        //this.animal = result;
      });
  }

  public doAdd() {
     let usuario = {id: 0, usuario: '', tipo: 1, senha: ''};
     
     const dialogRef = this.dialog.open(CadusuarioComponent, {
      width: '400px',
        data: usuario
      });

      dialogRef.afterClosed().subscribe(result => {
          this.loadData();
      });
  }
  public doDelete(usuario) {
       if (window.confirm("Deseja excluir o usuário selecionado?")) {
         this.request.delete("usuario","&id=" + usuario.id).subscribe(retorno => {
           let ret = retorno as any;
           if (ret.status == 'OK') {
             this.snackBar.open("Usuário excluído com sucesso!", null, {duration: 5000});
             this.loadData();
           } else {
             this.snackBar.open(ret.message, null, {duration: 5000});
           }
         }, error => {
           this.snackBar.open("Ocorreu um erro:" + error.message, null, {duration: 5000});
         })
       }     
     
  }

}
