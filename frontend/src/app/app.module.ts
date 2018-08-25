import { BrowserModule } from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChecklistModule } from './checklist/checklist.module';
import { KeycloakService } from './keycloak/keycloak.service';
import { HttpModule, RequestOptions, XHRBackend } from '@angular/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppNavbarComponent } from './app-navbar/app-navbar.component';
import {KeycloakHttp, keycloakHttpFactory} from './keycloak/keycloak.http';
import {ErrorHandlerImpl} from './ErrorHandlerImpl';
import {ToastModule} from 'ng2-toastr/ng2-toastr';
import {ToastService} from './toast-service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';


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
    ToastModule.forRoot(),
    BrowserAnimationsModule,
    NgbModule.forRoot()
  ],
  providers: [
    ErrorHandlerImpl,
    {
      provide: ErrorHandler,
      useClass: ErrorHandlerImpl
    },
    {
      provide: KeycloakHttp,
      useFactory: keycloakHttpFactory,
      deps: [XHRBackend, RequestOptions, KeycloakService]
    },
    KeycloakService,
    ToastService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
