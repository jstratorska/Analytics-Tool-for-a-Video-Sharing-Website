import {Component, OnInit} from '@angular/core';
import {IPlaylist} from 'src/app/shared/playlist';
import {IVideo} from 'src/app/shared/video';
import {ISelection} from 'src/app/shared/selection';
import {DataService} from '../data.service';
import {Constants} from 'src/app/shared/constants';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {

  playlists: IPlaylist[];
  videos: IVideo[];
  selectors: ISelection[] = [];
  colors: string[] = Constants.colors;
  statistics: string[] = Constants.statistics;
  chartParameters = [];
  chartCircumference = 2 * Math.PI * 300;

  constructor(private dataService: DataService, private activatedRoute: ActivatedRoute) {
  };

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.playlists = data.playlists;
      this.videos = data.videos;
    });

    this.addSelector();
    this.addSelector();
  }

  playlistSelectionChanged(event: any) {
    this.updateVideos(event.target.value, event.target.parentElement.parentElement.id);
  }

  videoSelectionUpdated(event: any) {
    this.selectors[event.target.parentElement.parentElement.id].selectedVideoId = event.target.value;
  }

  colorSelectionUpdated(event: any) {
    this.selectors[event.target.parentElement.parentElement.id].color = event.target.value;
  }

  statisticSelectionUpdated(event: any) {
    this.selectors[event.target.parentElement.parentElement.id].statistic = event.target.value;
  }

  updateVideos(playlistId: string, selectorId: number) {
    this.dataService.getVideoData(playlistId).subscribe(
      (data: IVideo[]) => this.selectors[selectorId].videos = data,
      (error: any) => console.log(error)
    );
  }

  addSelector() {
    this.selectors[this.selectors.length] = {playlists: this.playlists, videos: this.videos, statistic: this.statistics[0], color: this.colors[this.selectors.length], selectedVideoId: null};
  }

  deleteSelector(event) {
    let id = event.target.parentElement.parentElement.id;
    this.selectors = this.selectors.filter((selector, index) => index != id);
  }


  generateChart() {
    let total = 0;
    let individualVideoStatistic = [];
    this.chartParameters = [];

    this.selectors.forEach(selector => {
      let statistic = selector.statistic;
      let selectedVideo: IVideo = selector.videos.filter(video => video.id === selector.selectedVideoId)[0];
      total += selectedVideo[statistic];
      individualVideoStatistic.push(selectedVideo[statistic]);
    });

    let dashOffset = 0;
    let accumulatedPercentage = 0;
    this.selectors.forEach((selector, index) => {

      let percentage = individualVideoStatistic[index] / total;
      let dashArrayValue = this.calculateDashArrayValue(percentage);
      let angle = (1 - (accumulatedPercentage + percentage / 2) * 2) * Math.PI;
      accumulatedPercentage += percentage;

      this.chartParameters.push({
        dashArrayValue: dashArrayValue, dashOffset: dashOffset,
        color: selector.color, percentage: percentage,
        x: Math.sin(angle) * 150 + 300, y: Math.cos(angle) * 150 + 300
      });

      dashOffset -= dashArrayValue;
    });
  }

  calculateDashArrayValue(percentage: number): number {
    return percentage * this.chartCircumference;
  }
}
