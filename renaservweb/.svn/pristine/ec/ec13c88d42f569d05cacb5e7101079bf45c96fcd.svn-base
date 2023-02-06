import { Component, OnInit, ViewChild } from '@angular/core';
import {FormControl} from '@angular/forms';
import {Observable, timer} from 'rxjs';
import { RequestService } from '../request.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

const baseUrl = environment.production ? "/renaserv-war/" : "http://localhost:8080/renaserv-war/";    

interface Filtro {
   dataInicial:Date;
   dataFinal:Date;
   cliente:string;
   status:string;
   placa:string;
   pagina:number;
   exportar: boolean;
}


@Component({
  selector: 'app-listaocorrenciasfin',
  templateUrl: './listaocorrenciasfin.component.html',
  styleUrls: ['./listaocorrenciasfin.component.css']
})
export class ListaocorrenciasfinComponent implements OnInit {

 constructor(private request:RequestService, private router:Router) {


  }

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  displayedColumns: string[] = ['id', 'placa', 'cliente', 'data', 'evento', 'status', 'usuarioTratativa', 'dataTratativa'];
  eventos:[] = [];
  filteredClientes = [];
  stateCtrl = new FormControl();  
  
  filtro:Filtro = {
    dataInicial: new Date(),
    dataFinal: new Date(),
    cliente: "",
    placa: "",
    pagina: 1,
    status: "2",
    exportar: false
  }
  
  clientes = [];
  dataSource:MatTableDataSource<any>;

  ngOnDestroy() {
    
  }

  ngOnInit() {
    this.filtro.dataInicial.setDate(1);

  	this.request.get("listaclientes","").subscribe(data => {

  		this.clientes = data as [];

      this.clientes.map(cliente => {
          cliente.toString = function() {
            return cliente.nome;
          }
      });

  	});

    this.dataSource = new MatTableDataSource(this.eventos);
    //this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.loadData();
    

  }

  change() {
  	this.filteredClientes = this.clientes.filter(data => data.nome.toLowerCase().indexOf(this.filtro.cliente.toString().toLowerCase()) === 0);
  	 //console.log(this.filteredStates);
  }

  clienteSelected() {
    
  }

  public pesquisar() {
     this.paginator.pageIndex = 0;
     this.filtro.exportar = false;
     this.loadData();
  }

  public exportar() {
     this.filtro.exportar = true;
     this.loadData();
     this.filtro.exportar = false;
  }

  private loadData() {
    this.filtro.pagina = this.paginator.pageIndex;

    this.request.post("listaeventos",{
       dataInicio: this.filtro.dataInicial.getTime(),
       dataFinal: this.filtro.dataFinal.getTime(),
       cliente: this.filtro.cliente,
       pagina: (this.paginator.pageIndex+1),
       status: parseInt(this.filtro.status),
       placa: this.filtro.placa,
       exportar: this.filtro.exportar

     }).subscribe(data => {
       let retorno = data as any;
       if (retorno.ocorrencias) {
         this.eventos = data["ocorrencias"];   
         this.eventos.map(evento => {
           let e:any = evento;
           e.dataEvento = new Date();
           e.dataEvento.setTime(e.id.dataDados);
           e.dataCadD = new Date(e.dataCad);
           if (e.tratativa.data) {
              e.tratativa.dataD = new Date(e.tratativa.data);
           }
         });
         this.dataSource.data = this.eventos;
         console.log(this.eventos);

         this.paginator.pageSize = 50;
         this.paginator.length = data["qtd"];
       } 
       if (retorno.report) {
         window.open(baseUrl + "download?f=" + retorno.report);
       }
     });
  }

  pageChange() {
    this.loadData();
  }

  tratar(selected:any) {
    this.router.navigateByUrl("/principal/ocorrencia/" + selected.id + "/" + selected.veiculo + "/" + selected.evento + "/" + selected.dataDados);
  }

  public filtrarStatus() {
    this.pesquisar();
  }

}
