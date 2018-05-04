import { Component, OnInit } from '@angular/core';
import {KeycloakHttp} from '../keycloak/keycloak.http';
import {KeycloakService} from '../keycloak/keycloak.service';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html',
  styleUrls: ['./app-navbar.component.css']
})
export class AppNavbarComponent implements OnInit {
  title = 'Checklist Manager Web App';
  // TODO: msgs?

  isCollapsed = false;

  titleToken = 'User Information Obtained from the Token';
  titleAPIList = 'User List obtained via Keycloak HTTP API call';
  titleAPIListExampleRequestHeader = 'Example Request Headers for Keycloak HTTP API call';

  isTokenCardVisible = false;
  isAPICardsVisible = false;

  username: string;
  fullName: string;

  constructor(private keycloakHttp: KeycloakHttp ) { }

  ngOnInit() {

    this.username = KeycloakService.getUsername();
    this.fullName = KeycloakService.getFullName();
  }

  reset(): void {
    this.isTokenCardVisible = false;
    this.isAPICardsVisible = false;
  }

  getUserInfoFromToken(): void {
    this.isTokenCardVisible = !this.isTokenCardVisible;
  }

  // getUsersFromJsonApi(): void {
  //   this.keycloakHttp.get(environment.usersApiRootUrl)
  //     .map(response => response.json())
  //     .subscribe(
  //       result => {
  //         this.usersArray = result;
  //         this.isAPICardsVisible = true;
  //       },
  //       error => console.log(error),
  //       () => console.log('Request Completed :: AppComponent.getUsersFromJsonAPI()')
  //     );
  // }

  logout(): void {
    KeycloakService.logout();
  }

}
