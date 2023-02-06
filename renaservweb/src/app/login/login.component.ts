import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RequestService} from '../request.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  login: string = '';
  senha: string = '';	

  constructor(private http: HttpClient, private request:RequestService, private snackBar: MatSnackBar, private router:Router) { }



  ngOnInit() {
    this.request.isInDevMode();
  }

  entrar(){
  	let sub = {login: this.login, senha: this.senha};
    let url = "./login";

    if (!this.request.isInDevMode()) {
      url = "http://localhost:8080/renaserv-war/login";
    }
  	this.http.post(url, sub).subscribe(data => {

  		if (data["token"]) {
  			this.request.login = data;
      	    this.router.navigateByUrl("/principal");

			localStorage.setItem("loginInfo", JSON.stringify(this.request.login) );

  		} else {
  			this.snackBar.openFromComponent(LoginComponent, {
		      duration: 5000,
		    });
  		}


  	}, error => {

  		console.log(error);

  		if (error.status == 403) {
  			this.snackBar.open("Usuário ou senha inválidos","", {
		      duration: 5000,
		    });	
  		} else {
  			this.snackBar.open(error.message,"", {
		      duration: 5000,
		    });	
  		}
  		

  	});
  }

}
