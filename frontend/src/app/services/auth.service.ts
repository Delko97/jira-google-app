import {ChangeDetectorRef, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Router} from '@angular/router';
import {User} from '../auth/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private static TOKEN_KEY = 'currentUser';
  public isLogged = false;
  public isShown = false;

  constructor(private http: HttpClient,
              private router: Router) {
    console.log(this.isLogged);
  }

  public getToken(): string | null {
    return localStorage.getItem(AuthService.TOKEN_KEY);
  }

  login(username: string, password: string) {
    return this.http.post('http://localhost:8081/login', {username, password})
      .pipe(
        map((user: User) => {
          if (user && user.token) {
            localStorage.setItem(AuthService.TOKEN_KEY, user.token);
            localStorage.setItem('isLoggedIn', 'true');
            this.router.navigate(['/dashboard']);
            this.isLogged = true;
            console.log(this.isLogged);
            console.log('token', user.token);
          }
          return user;
        })
      );
  }

  onLogout() {
    return this.logout(() => {
      this.isShown = true;
      console.log('isShown - before', this.isShown);
      setTimeout(() => {
        this. isShown = false;
        // this.cd.detectChanges();
        console.log('isShown', this.isShown);
      }, 5000);
    });
  }

  logout(callback?: () => void) {
    // remove user from local storage to log user out
    localStorage.removeItem(AuthService.TOKEN_KEY);
    localStorage.setItem('isLoggedIn', 'false');
    this.router.navigate(['/login']);
    this.isLogged = false;
    callback();
  }

  public isLoggedIn(): boolean {
    let status = false;
    if (localStorage.getItem('isLoggedIn') === 'true') {
      status = true;
    } else {
      status = false;
    }
    return status;
  }


}
