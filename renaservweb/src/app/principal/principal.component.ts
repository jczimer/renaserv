import { Component, OnInit, ViewChild } from '@angular/core';
import {RequestService} from '../request.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.css']
})
export class PrincipalComponent implements OnInit {

  menu = true;
  interval;
  usuario:any = {}
  ultimaVerificacao:number = 1;
  
  constructor(private request:RequestService, private router:Router) { }

  ngOnInit() {

  	this.usuario = this.request.login;
    this.loadOcorrencias();
    this.startTimer();

  }

  private startTimer():void{
    this.interval = setInterval(() => {
      this.loadOcorrencias();
    },60000);
  }

  private loadOcorrencias():void{
    let filter = {
      dataInicio: this.ultimaVerificacao,
      dataFinal: new Date().getTime(),
      pagina: 1,
      cliente: 0,
      status: 0
    };
    this.request.post("listaeventos", filter).subscribe(ret => 
        {
          let resp:any = ret;
          if (resp.status == 'OK') {
            let ocorrencias = resp.ocorrencias as [];
            if (ocorrencias.length > 0) {
              this.playSiren();
            }
            this.ultimaVerificacao = new Date().getTime();
          }          
        }
    );
  }

  public playSiren() {
    let audio = <HTMLAudioElement> document.getElementById("audio");
    audio.play();
  }

  public doExit(){
    localStorage.removeItem("loginInfo");
  	this.request.login = null;
  	this.router.navigateByUrl("/");
    clearInterval(this.interval);
  }

  public navigate(url:string) {
    this.router.navigateByUrl(url);
  }

}
