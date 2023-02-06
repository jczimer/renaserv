import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { NgForm } from '@angular/forms';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {RequestService} from '../request.service';
import {MatSnackBar} from '@angular/material/snack-bar';


@Component({
  selector: 'app-cadusuario',
  templateUrl: './cadusuario.component.html',
  styleUrls: ['./cadusuario.component.css']
})
export class CadusuarioComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public usuario: any,
    private dialogRef: MatDialogRef<CadusuarioComponent>,    
    private request:RequestService,
    private snackBar:MatSnackBar  
  ) { }

  @ViewChild('formCadastro', {static: false}) formCadastro: NgForm;
  tipos = [{ tipo: 0, descricao: 'Administrador' }, {tipo: 1, descricao: 'Operador'}];

  ngOnInit() {

  }

  public salvar() {
    this.request.post("/usuario", this.usuario).subscribe((retorno) => {
      let ret = retorno as any;
      if (ret.status == 'OK'){
        this.dialogRef.close();
        this.snackBar.open("Atualizado com sucesso!", null, {duration: 5000});
      } else {
        this.snackBar.open("Ocorreu um erro: " + ret.message, null, {duration: 5000});
      }
    }, error => {
      this.snackBar.open("Ocorreu um erro: " + error.message, null, {duration: 5000});
      console.log(error);
    });
  }

}
