import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Constants} from '../shared/constants';
import {map} from 'rxjs/operators';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authToken: string;
  private authenticated = new BehaviorSubject<boolean>(false);
  authenticated$ = this.authenticated.asObservable();

  private username = new BehaviorSubject<string>("");
  username$ = this.username.asObservable();

  constructor(private http: HttpClient) { }

  getAuthorizationToken() {
    return this.authToken;
  }

  setAuthorizationToken(authToken: string) {
    this.authToken = authToken;
    this.isAuthenticated = true;
  }

  public set isAuthenticated(value: boolean) {
    this.authenticated.next(value);
  }

  public get isAuthenticated(): boolean {
    return this.authenticated.getValue();
  }

  public set authenticatedUsername(value: string) {
    this.username.next(value);
  }

  public get authenticatedUsername(): string {
    return this.username.getValue();
  }

  login(username: string, password: string) {
    return this.http.post<any>(Constants.loginUrl, {username: username, password: password}, {observe: 'response'}).pipe(
      map(data => {
        this.authenticatedUsername = username;
        this.setAuthorizationToken(data.headers.get("Authorization"));
      }));
  }

  register(username: string, password: string) {
    return this.http.post<any>(Constants.registerUrl, {username: username, password: password});
  }

  logout() {
    this.isAuthenticated = false;
    this.authToken = "";
    this.authenticatedUsername = "";
  }
}
