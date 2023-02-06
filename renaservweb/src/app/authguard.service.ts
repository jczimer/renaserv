import { Injectable } from '@angular/core';
import { RequestService } from './request.service';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthguardService implements CanActivate {

  constructor(private reqService:RequestService) { }

  canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ): boolean {
  	let canActivate = this.reqService.login;
	  
	if (!canActivate)   {
		let loginInfo = localStorage.getItem("loginInfo");
		if (loginInfo) {
			canActivate = JSON.parse(loginInfo);
			this.reqService.login = canActivate;
		}
	}

  	if (canActivate && canActivate.tipo == 1) {
  		let url = route.url[0];
  		console.log(url);
  		if (url.path == 'cliente') {
  			return false;
  		}
  		if (url.path == 'tipoevento') {
  			return false;
  		}
  		if (url.path == 'relatorios') {
  			return false;
  		}
      if (url.path == 'historico') {
        return false;
      }
  		
  	}

  	return canActivate != null;
  }
}
