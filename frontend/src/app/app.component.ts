import {Component, OnInit, ViewContainerRef} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {ToastsManager} from 'ng2-toastr';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private router: Router, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    // this.router.navigate(['/checklist']);
  }

}
