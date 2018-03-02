import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html',
  styleUrls: ['./app-navbar.component.css']
})
export class AppNavbarComponent implements OnInit {
  title = 'Checklist Manager Web App';
  // TODO: msgs?

  isCollapsed = false;

  constructor() { }

  ngOnInit() {
  }

}
