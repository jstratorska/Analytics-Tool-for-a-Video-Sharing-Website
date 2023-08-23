import {Injectable} from '@angular/core';
import {AuthService} from './auth.service';
import {HttpRequest, HttpHandler, HttpInterceptor} from '@angular/common/http';
import {Constants} from 'src/app/shared/constants';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private auth: AuthService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        if ((req.url != Constants.loginUrl) && (req.url != Constants.registerUrl)) {
            const authToken = this.auth.getAuthorizationToken();
            const authReq = req.clone({setHeaders: {Authorization: authToken}});
            return next.handle(authReq);
        }

        return next.handle(req);
    }
}