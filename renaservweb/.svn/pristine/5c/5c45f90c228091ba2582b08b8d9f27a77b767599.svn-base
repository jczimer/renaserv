import { Component, OnInit, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {RequestService} from '../request.service';

@Component({
  selector: 'app-listaveiculos',
  templateUrl: './listaveiculos.component.html',
  styleUrls: ['./listaveiculos.component.css']
})
export class ListaveiculosComponent implements OnInit {

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  filtro:any = {argument: ''};

  filteredClientes = [];
  clientes = [];
  veiculos = [];
  displayedColumns: string[] = ['id', 'placa', 'modelo','proprietario','cliente', 'sistema', 'dataatualizacao', 'coordenadas'];
  dataSource:MatTableDataSource<any>;


  constructor(private request:RequestService) { }

  ngOnInit() {

    let f:any = this.request.getFilter("listaveiculos");
    if (f) {
      this.filtro = f;
      this.paginator.pageIndex = f.page;
      this.paginator.pageSize = f.limit;
      this.loadData();
    }
  	this.dataSource = new MatTableDataSource(this.veiculos);
    this.loadClientes();
  }

  public pesquisar():void {
  	 this.paginator.pageIndex = 0;
  	 this.loadData();
  }

  public loadData() {
  	const page = this.paginator.pageIndex+1;
    let cliente = this.filtro.cliente ? this.filtro.cliente.id : 0;
  	this.request.post("listaveiculos", {
      page: page,
      limit: this.paginator.pageSize,
      argument: this.filtro.argument,
      cliente: cliente
    }).subscribe(ret => {
  	 	console.log(ret);
  	 	let resp:any = ret as any;
      let veiculos = resp.veiculos;
      veiculos.map(veiculo => {
          if (veiculo.posicao.dataDados) {
            veiculo.posicao.dataDados = new Date(veiculo.posicao.dataDados);
          } else {
            veiculo.posicao.dataDados = null;
          }
      }); 


  	 	this.dataSource.data = resp.veiculos;
  	 	this.paginator.length = resp.qtd;
  	 });

    this.filtro.page = this.paginator.pageIndex;
    this.filtro.limit = this.paginator.pageSize;
    this.request.storeFilter("listaveiculos", this.filtro);
  }

  public loadClientes() {
    this.request.get("listaclientes", "").subscribe(ret => {
       let l:any = ret;
       this.clientes = l;

       this.clientes.map(cliente => {
          cliente.toString = function() {
            return cliente.nome;
          }
      });
    });
  }

  public change() {
     this.filteredClientes = this.clientes.filter(data => data.nome.toLowerCase().indexOf(this.filtro.cliente.toString().toLowerCase()) === 0);
  }

  pageChange() {
    this.loadData();
  }

  public ignOn(veiculo:any):boolean {
    return (veiculo.posicao.io & 0x01) == 0x01;
  }

}
