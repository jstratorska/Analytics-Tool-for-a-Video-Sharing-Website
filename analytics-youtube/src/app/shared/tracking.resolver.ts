import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, RouterStateSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {DataService} from './data.service';

@Injectable({providedIn: 'root'})
export class TrackingResolver implements Resolve<any> {
    constructor(private dataService: DataService) { }

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<any> | Promise<any> | any {

        return this.dataService.getTrackingList();
    }
}