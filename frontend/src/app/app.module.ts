import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChecklistModule} from './checklist/checklist.module';
import { HttpModule } from '@angular/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppNavbarComponent } from './app-navbar/app-navbar.component';


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
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
