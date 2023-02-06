import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {RequestService} from '../request.service';

@Component({
  selector: 'app-listaclientes',
  templateUrl: './listaclientes.component.html',
  styleUrls: ['./listaclientes.component.css']
})
export class ListaclientesComponent implements OnInit {

  constructor(private request:RequestService) { }

  clientes = [
  	{id: 1, nome: 'Jean', system: 1, _user: 'a', _key: 'v', _system: 1},
  	{id: 1, nome: 'Jean', system: 1, _user: '', _key: 'setView(center: LatLng, zoom: number)'},
  	{id: 1, nome: 'Jean', system: 1, _user: 'a', _key: 'as'}
  ];
  displayedColumns: string[] = ['id', 'system', 'nome','user','key', 'status'];
  dataSource:MatTableDataSource<any>;


  ngOnInit() {
  	this.dataSource = new MatTableDataSource(this.clientes);

  	this.loadData();
  }

  private loadData() {
  	  this.request.get("listaclientes","").subscribe(ret => {
  	  	let data = ret as [];
  	  	data.map(item => {
  	  		let l = item as any;	
  	  		switch (l._system) {
  	  		case 1:
  	  			l.plataforma = "Rodar";
  	  			break;
  	  		case 2:
  	  			l.plataforma = "STC";
  	  			break;
  	  		case 3:
  	  			l.plataforma = "Multiportal";
  	  			break;
  	  		}  	  		
  	  	});
  	  	this.dataSource.data = ret as any;  	  	 
  	  });
  }

}
