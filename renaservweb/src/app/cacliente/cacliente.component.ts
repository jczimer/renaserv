import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { RequestService } from '../request.service';
import { ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-cacliente',
  templateUrl: './cacliente.component.html',
  styleUrls: ['./cacliente.component.css']
})
export class CaclienteComponent implements OnInit {

  constructor(private request:RequestService, private route:ActivatedRoute, private snackBar:MatSnackBar, private changeRef:ChangeDetectorRef) { }
  cliente:any = {nome: ''}
  tiposEventos = [];
  eventos = [];
  eventosMonitorados = [];
  fileToUpload: File;
  fileLogo: string;

  @ViewChild('formCadastro', {static: false}) formCadastro: NgForm;
  

  ngOnInit() {
	  let id = this.route.snapshot.paramMap.get("id");

  	this.request.get("listatipoeventos","").subscribe(ret => {

  		let data:any = ret;	
  		if (data.status == "OK") {

  			this.tiposEventos = data.eventos;

  			this.loadCliente(id);

  		}

  	});

  	
  	

  }

  public loadCliente(id) {
     this.request.get("cliente","&id=" + id).subscribe(ret => {
          const l:any = ret;
          this.cliente = l.credential;

          const eventosClientes = this.cliente.eventos as [];

          this.bindEventos(this.tiposEventos, eventosClientes);

          let baseUrl = this.request.getBaseUrl();
          this.fileLogo =  baseUrl.substring(0, baseUrl.length-4) + "logo?filename=" + this.cliente.id + "/" + this.cliente.image;

        });
  }

  public bindEventos(eventos, eventosClientes) {
  	   	 	
  	 	eventos.map(e1 => {
  	 			const e = e1 as any;	  	 		
	  	 		e.checked = false;
  	 			for(let i = 0; i < eventosClientes.length; i++) {
  	 				let eventoCliente = eventosClientes[i] as any;
  	 				if (eventoCliente.id == e.id) {
  	 					e.checked = true;
  	 					break;
  	 				}
  	 			}
  	 	});
  	 	
  	 	this.eventos = eventos;

  	 	this.changeRef.detectChanges();

  	 	 
  }

  public changeEvento(evento) {
  	// this.eventosMonitorados = this.eventos.filter(e => e.checked);
  	// console.log(this.eventosMonitorados);
  	//console.log(this.eventos);
  }

  public set status(value:string) {
  	 this.cliente.status = parseInt(value);
  }

  public get status():string{
  	return this.cliente.status + "";
  }

  public set sistema(value:string){
  	 this.cliente._system = parseInt(value);
  }

  public get sistema():string{
  	return this.cliente._system + "";
  }

  public salvar() {
  	 this.cliente.eventos = this.eventos.filter(e => e.checked);
  	 this.request.post("cliente", this.cliente).subscribe(ret => {
  	 	const l:any = ret as any;
  	 	if (l.status=="OK") {
  	 		this.cliente = l.credential;
  	 		this.snackBar.open("Salvo com sucesso!", "", {duration: 5000});
  	 	}
  	 }, error => {
  	 	console.log(error);
  	 	this.snackBar.open("Ocorreu um erro ao tentar salvar!", "", {duration: 5000});
  	 });
  }

  public salvarImagem() {
      const formData: FormData = new FormData();
      formData.append('fileKey', this.fileToUpload, this.fileToUpload.name);
      formData.append('cliente', this.cliente.id);

      this.request.post("uploadlogo", formData).subscribe(ret => {

          this.snackBar.open("Imagem salva com sucesso", "", {duration: 5000});

          this.loadCliente(this.cliente.id);

      });
  }

  public handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }

}
