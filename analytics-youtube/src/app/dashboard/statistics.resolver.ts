import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, RouterStateSnapshot, Resolve} from '@angular/router';
import {Observable, of} from 'rxjs';
import {DataService} from '../shared/data.service';
import {AuthService} from '../core/auth.service';

@Injectable({providedIn: 'root'})
export class StatisticsResolver implements Resolve<any> {
    constructor(private dataService: DataService, private auth: AuthService) { }

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

        if (this.auth.isAuthenticated) {
            return this.dataService.getVideoStatistics();
        }
        else {
            return of([]);
        }
    }
}