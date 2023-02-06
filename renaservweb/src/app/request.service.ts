import { Injectable } from '@angular/core'
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';

const baseUrl = environment.production ? "/renaserv-war/api/" : "http://localhost:8080/renaserv-war/api/";		

export interface IHash {
    [details: string] : string;
} 

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  private loginO;
  private filter = new Map<string,object>();

  constructor(private http:HttpClient) { }

  public set login(loginO) {
  	this.loginO = loginO;
  }

  public get login() {
  	return this.loginO;
  }

  public post(url:string,data) {
    data.token = this.loginO.token;
  	return this.http.post(baseUrl + url + "?token=" + this.loginO.token, data);
  }

  public get(url:string, params:string) {
    return this.http.get(baseUrl + url + "?token=" + this.loginO.token + params);
  }

  public delete(url:string, params:string) {
    return this.http.delete(baseUrl + url + "?token=" + this.loginO.token + params);
  }

  public storeFilter(name:string, object:object) {
      this.filter.set(name, object);
  }

  public getFilter(name:string):object{
    return this.filter.get(name);
  }

  public isInDevMode() {
    console.log(environment.production);
    return environment.production;
  }

  public getBaseUrl() {
    return baseUrl;
  }

}
