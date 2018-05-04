import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChecklistModule } from './checklist/checklist.module';
import { KeycloakService } from './keycloak/keycloak.service';
import { HttpModule, RequestOptions, XHRBackend } from '@angular/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppNavbarComponent } from './app-navbar/app-navbar.component';
import {KeycloakHttp, keycloakHttpFactory} from './keycloak/keycloak.http';


@NgModule({
  declarations: [
    AppComponent,
    AppNavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ChecklistModule,
    HttpModule,
    NgbModule.forRoot()
  ],
  providers: [
    {
      provide: KeycloakHttp,
      useFactory: keycloakHttpFactory,
      deps: [XHRBackend, RequestOptions, KeycloakService]
    },
    KeycloakService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
