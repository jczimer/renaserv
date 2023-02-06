import { NgModule } from '@angular/core';
import { Routes, RouterModule, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthguardService } from './authguard.service';
import { ListaeventosComponent } from './listaeventos/listaeventos.component';
import { TratativaocorrenciaComponent } from './tratativaocorrencia/tratativaocorrencia.component';
import { PrincipalComponent } from './principal/principal.component';
import { ListaclientesComponent } from './listaclientes/listaclientes.component';
import { ListaveiculosComponent } from './listaveiculos/listaveiculos.component';
import { ListausuarioComponent } from './listausuario/listausuario.component';
import { ListatipoeventosComponent } from './listatipoeventos/listatipoeventos.component';
import { CaclienteComponent } from './cacliente/cacliente.component';
import { CadveiculoComponent } from './cadveiculo/cadveiculo.component';
import { CadtipoeventosComponent } from './cadtipoeventos/cadtipoeventos.component';
import { CadusuarioComponent } from './cadusuario/cadusuario.component';
import { ListaocorrenciasfinComponent } from './listaocorrenciasfin/listaocorrenciasfin.component';


const routes: Routes = [
	{ path: '', component: LoginComponent },
	{ path: 'principal', component: PrincipalComponent, canActivate: [AuthguardService], children: [
		{ path: '', component: ListaeventosComponent },
		{ path: 'listaeventos', component: ListaeventosComponent, canActivate: [AuthguardService] },
		{ path: 'historico', component: ListaocorrenciasfinComponent, canActivate: [AuthguardService] },
		{ path: 'cliente/edit/:id', component: CaclienteComponent, canActivate: [AuthguardService] },
		{ path: 'veiculo/edit/:credential/:id', component: CadveiculoComponent, canActivate: [AuthguardService] },
		{ path: 'ocorrencia/:id/:veiculo/:evento/:dataDados', component: TratativaocorrenciaComponent, canActivate: [AuthguardService] },
		{ path: 'cliente/lista', component: ListaclientesComponent },
		{ path: 'veiculo/lista', component: ListaveiculosComponent, canActivate: [AuthguardService] },
		{ path: 'usuario/lista', component: ListausuarioComponent, canActivate: [AuthguardService] },
		{ path: 'tipoevento/lista', component: ListatipoeventosComponent, canActivate: [AuthguardService] },
		{ path: 'tipoevento/edit/:id', component: CadtipoeventosComponent, canActivate: [AuthguardService] },
		{ path: 'usuario/edit/:id', component: CadusuarioComponent, canActivate: [AuthguardService] },
	] }	
	
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule],
  providers: [AuthguardService]
})
export class AppRoutingModule { }
