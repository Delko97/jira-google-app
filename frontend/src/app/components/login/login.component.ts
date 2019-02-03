import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';
import {AuthService} from '../../services/auth.service';
import {catchError, finalize} from 'rxjs/internal/operators';
import {empty} from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  error = '';
  isLogged = false;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) {
    console.log('loginIsShow', this.getIsShown());
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    // get return url from route parameters or default to '/'
    this.returnUrl = 'dashboard';
    this.isLogged = this.authService.isLoggedIn();
    console.log('isLogged', this.isLogged);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authService.login(this.f.username.value, this.f.password.value)
      .pipe(
        first(),
        catchError(err => {
          // this.alertService.error(error);
          this.loading = false;
          return empty();
        }),
        finalize(() => {
          this.loading = false;
          console.log('FINALIZE', this.loading);
        })
      )
      .subscribe(
        data => {
          console.log('SUBSCRIBE', this.returnUrl, data);
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.error = error;
          this.loading = false;
        }
      );
  }

  public getIsShown() {
    return this.authService.isShown;
  }
}


