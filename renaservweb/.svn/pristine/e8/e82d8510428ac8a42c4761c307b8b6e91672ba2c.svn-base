import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {RequestService} from '../request.service';

@Component({
  selector: 'app-cadveiculo',
  templateUrl: './cadveiculo.component.html',
  styleUrls: ['./cadveiculo.component.css']
})
export class CadveiculoComponent implements OnInit {

  veiculo:any = {   };

  contatos:Array<any>;

  constructor(private route:ActivatedRoute, private request:RequestService) { }

  ngOnInit() {
  	let id:number = parseInt(this.route.snapshot.paramMap.get("id"));
    let credential:number = parseInt(this.route.snapshot.paramMap.get("credential"));
  	this.loadData(credential, id);

  }

  private loadData(credential:number, id:number) {
  	 this.request.get("veiculo", "&credential="+ credential +"&id=" + id).subscribe(ret => {
  	 	let retorno:any = ret;
  	 	if (retorno.status == "OK") {

  	 		this.veiculo = retorno.veiculo;	
        this.contatos = this.veiculo.proprietario.contatos.split("\n");

  	 	}
  	 });
  }

}
