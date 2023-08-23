import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {IPlaylist} from './playlist';
import {IVideo} from './video';
import {ITimeData} from './time-data';
import {Constants} from './constants';
import {ICategory} from 'src/app/shared/category';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { };

  getPlaylistData(): Observable<IPlaylist[]> {
    let url = Constants.playlistDataUrl;
    return this.http.get<IPlaylist[]>(url);
  }

  getVideoData(playlistId: string): Observable<IVideo[]> {
    let url = `${Constants.videoDataUrl}/${playlistId}`;
    return this.http.get<IVideo[]>(url);
  }

  getVideoStatistics(): Observable<any> {
    let url = Constants.videoStatistics;
    return this.http.get<any>(url);
  }

  getVideoNumber(): Observable<number> {
    let url = Constants.videoNumber;
    return this.http.get<number>(url);
  }

  getCategories(): Observable<ICategory[]> {
    let url = Constants.videoCategoryDataUrl;
    return this.http.get<ICategory[]>(url);
  }

  getTrackedData(startTime: string, endTime: string, videoId: string, statistic: string): Observable<ITimeData[]> {
    let url = `${Constants.trackingDataUrl}?startTime=${startTime}&endTime=${endTime}&videoId=${videoId}&statistic=${statistic}`;
    return this.http.get<ITimeData[]>(url);
  }

  getTrackingList() {
    let url = Constants.trackingUrl;
    return this.http.get<ITimeData[]>(url);
  }

  setTracking(videoId: string, statistic: string) {
    let url = `${Constants.startTrackingUrl}?videoId=${videoId}&statistic=${statistic}`;
    return this.http.post<ITimeData[]>(url, {});
  }

  deleteTacking(videoId: string, statistic: string) {
    let url = `${Constants.stopTrackingUrl}?videoId=${videoId}&statistic=${statistic}`;
    return this.http.post<ITimeData[]>(url, {});
  }

  getForecastData(numberOfPredictions: number, videoId: string, statistic: string) {
    let url = `${Constants.forecastingUrl}?predictions=${numberOfPredictions}&videoId=${videoId}&statistic=${statistic}`;
    return this.http.get<ITimeData[]>(url);
  }
}
