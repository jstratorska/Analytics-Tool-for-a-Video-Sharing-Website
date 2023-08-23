import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from 'src/app/core/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isAuthenticated: boolean;

  constructor(private auth: AuthService, private router: Router) { }

  logout(event) {
    event.preventDefault();
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  ngOnInit(): void {
    this.auth.authenticated$.subscribe(
      data => this.isAuthenticated = data
    );
  }

}
