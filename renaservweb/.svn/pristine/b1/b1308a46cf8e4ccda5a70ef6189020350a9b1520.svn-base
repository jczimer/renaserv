import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { RequestService } from '../request.service';
import * as L from 'leaflet';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-tratativaocorrencia',
  templateUrl: './tratativaocorrencia.component.html',
  styleUrls: ['./tratativaocorrencia.component.css']
})
export class TratativaocorrenciaComponent implements OnInit {

  private id:string;	
  private veiculo:string;
  private evento:string;
  private dataDados:string;

  map:L.Map;
  contatos:Array<any>;
  finalizado:boolean;
  descritivo:string;
  endereco:string;
  enderecoAtual:string;
  options = {
  layers: [
      L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 18, attribution: '...' })
    ],
    zoom: 5,
    center: L.latLng(46.879966, -121.726909)
  };

  ocorrencia:any = { veiculo: {}, evento: {}, posicaoAtual: {}, cliente: {}, ocorrencia: {}, id: {}, proprietario: {} };  
  logs:any[] = [];

  constructor(private route:ActivatedRoute, 
    private router:Router, 
    private request:RequestService, 
    private http:HttpClient,
    private snackBar:MatSnackBar

    ) { 
  		
  }

  ngOnInit() {
  	 this.id = this.route.snapshot.paramMap.get('id');
  	 this.veiculo = this.route.snapshot.paramMap.get('veiculo');
  	 this.evento = this.route.snapshot.paramMap.get('evento');
  	 this.dataDados = this.route.snapshot.paramMap.get('dataDados');
     
     const id = {cliente: this.id, veiculo: this.veiculo, evento: this.evento, dataDados: this.dataDados};
     
     this.loadData();

     const ocorrencia = {id: id, tratativa: "Visualizou a ocorrência", finalizar: false};
         this.request.post("tratarocorrencia", ocorrencia).subscribe(ret => {
             const retorno:any = ret;
             console.log("retorno: " + retorno.status);
             
             this.loadLogs();
     });
  }

  private loadData() {
    this.request.get("/getocorrencia", "&cliente=" + this.id + "&veiculo=" + this.veiculo + "&evento=" + this.evento + "&dataDados=" + this.dataDados).subscribe(retorno => {

       const ret:any = retorno;
       if (ret.status == "OK") {
          this.ocorrencia = ret.ocorrencia; 
          if (this.ocorrencia.proprietario.contatos){
            this.contatos = this.ocorrencia.proprietario.contatos.split("\n");
          } else {
            this.contatos = [];
          }
          console.log(this.ocorrencia);
          this.getEndereco();
          this.getEnderecoAtual();

          this.map.setView([this.ocorrencia.posicaoAtual.lat, this.ocorrencia.posicaoAtual.lon],13);

          L.marker([this.ocorrencia.posicaoAtual.lat, this.ocorrencia.posicaoAtual.lon], {icon: L.icon({
            iconUrl: 'assets/ponto1.png'
          })
        }).addTo(this.map);
        this.map.invalidateSize();  



       }


    });

    
  }

  public get disabled():boolean{
    return this.descritivo && this.descritivo.length == 0;
  }

  public get usuario():string {
    return "Teste";
  }

  public get link():string{
    return "https://www.google.com/maps/preview?q=" +this.ocorrencia.lat + "," + this.ocorrencia.lon;
  }

  public get linkAtual():string{
    return "https://www.google.com/maps/preview?q=" +this.ocorrencia.posicaoAtual.lat + "," + this.ocorrencia.posicaoAtual.lon;
  }

  private getEndereco():void{
    if (this.ocorrencia.lon) {
      this.http.get("https://nominatim.openstreetmap.org/reverse?format=json&lat=" + this.ocorrencia.lat + "&lon=" + this.ocorrencia.lon).subscribe(ret =>{
        this.endereco = ret["display_name"];
      });    
    }
  }


  private getEnderecoAtual():void{
    if (this.ocorrencia.posicaoAtual.lat) {
      console.log(this.ocorrencia.posicaoAtual);
      this.http.get("https://nominatim.openstreetmap.org/reverse?format=json&lat=" + this.ocorrencia.posicaoAtual.lat + "&lon=" + this.ocorrencia.posicaoAtual.lon).subscribe(ret =>{
        this.enderecoAtual = ret["display_name"];
      });    
    }
  }

  public onMapReady(map: L.Map) {
    this.map = map;
  }

  public salvar(finalizar:boolean):void{
     const id = {cliente: this.id, veiculo: this.veiculo, evento: this.evento, dataDados: this.dataDados};
     const ocorrencia = {id: id, tratativa: this.descritivo, finalizar: finalizar};
     this.request.post("tratarocorrencia", ocorrencia).subscribe(ret => {
         const retorno:any = ret;
         if (retorno.status == "OK") {

           this.snackBar.open("Tratativa registrada com sucesso!","", {
             duration: 5000,
           });  
           this.router.navigateByUrl("/principal/listaeventos");

         }
     });
  }

  public loadLogs() {
    this.request.get("listalogs", "&credential="+ this.id +"&veiculo="+ this.veiculo + "&evento="+ this.evento +"&dataDados=" + this.dataDados).subscribe(ret => {
        if (ret["status"] == "OK") {
          this.logs = ret["logs"];  
        }
    });
  }

  public statusIgnicao(io:number):string{
      if ((io & 0x01) == 0x01) {
        return "Ligada";
      }
      return "Desligda";
  }


}
