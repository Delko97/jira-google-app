import {Component, OnInit} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../services/auth.service';
import {LoginComponent} from '../login/login.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  navbarCollapsed = true;

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
  }

  onLogout() {
    this.authService.onLogout();
  }
  isLogged() {
    return this.authService.isLoggedIn();
  }
}


