import {Component, OnInit} from '@angular/core';
import {IPlaylist} from 'src/app/shared/playlist';
import {IVideo} from 'src/app/shared/video';
import {IGraphSelector} from './graph-selector';
import {DataService} from '../data.service';
import {Constants} from '../constants';
import {ICategory} from 'src/app/shared/category';
import {ITimeData} from 'src/app/shared/time-data';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css']
})
export class GraphComponent implements OnInit {

  data = "";
  playlists: IPlaylist[];
  videos: IVideo[];
  categories: ICategory[];
  colors: string[] = Constants.colors;
  statistics: string[] = Constants.statistics;
  selector: IGraphSelector;
  xPoints = [];
  yPoints = [];
  xLabels = [];
  yLabels = [];
  canvasWidth = 1000;
  canvasHeight = 400;
  minWidth = 50;
  minHeight = 50;
  xOffset = 100;
  yOffset = 50;
  showGraph = false;

  constructor(private dataService: DataService, private activatedRoute: ActivatedRoute) { };

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.playlists = data.playlists;
      this.videos = data.videos;
      this.categories = data.categories;
    });
    this.selector = {categories: this.categories, playlists: this.playlists, videos: this.videos, statistic: this.statistics[0], color: this.colors[0], selectedVideoId: null};
  }

  categorySelectionChanged(event: any) {
    let categoryId = event.target.value;
    this.selector.videos = this.selector.videos.filter(video => video.categoryId == categoryId)
  }

  playlistSelectionChanged(event: any) {
    this.updateVideos(event.target.value);
  }

  videoSelectionUpdated(event: any) {
    this.selector.selectedVideoId = event.target.value;
  }

  colorSelectionUpdated(event: any) {
    this.selector.color = event.target.value;
  }

  statisticSelectionUpdated(event: any) {
    this.selector.statistic = event.target.value;
  }

  updateVideos(playlistId: string) {
    this.dataService.getVideoData(playlistId).subscribe(
      (data: IVideo[]) => this.videos = data,
      (error: any) => console.log(error)
    );
  }

  generateGraph() {
    this.data = "";
    this.xPoints = [];
    this.yPoints = [];
    let now = new Date();
    let aDayAgo = new Date(now);
    aDayAgo.setUTCHours(aDayAgo.getUTCHours() - 24);

    this.dataService.getTrackedData(aDayAgo.toISOString(), now.toISOString(), this.selector.selectedVideoId, this.selector.statistic).subscribe(
      (timeData: ITimeData[]) => {
        timeData.sort((a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());
        let timeDataMilliseconds = timeData.map(data => new Date(data.timestamp).getTime());
        let values = timeData.map(data => data.value);

        let maxX = Math.max(...timeDataMilliseconds);
        let minX = Math.min(...timeDataMilliseconds);

        let maxY = Math.max(...values);
        let minY = Math.min(...values);

        let height = this.canvasHeight - 2 * this.minHeight;
        let width = this.canvasWidth - 2 * this.minWidth;

        let valueXDiff = maxX - minX;
        let valueYDiff = maxY - minY;

        this.xPoints = [];
        this.yPoints = [];
        this.xLabels = [];
        this.yLabels = [];

        for (var i = 0; i < timeDataMilliseconds.length; i++) {
          let x = width * (timeDataMilliseconds[i] - minX) / valueXDiff + this.minWidth + this.xOffset;
          let y = this.canvasHeight - (height * (values[i] - minY) / valueYDiff + this.minHeight);
          this.xPoints.push(x);
          this.yPoints.push(y);
          this.data += `${x},${y} `;
        }

        this.setLabels(4, 4, minX, minY, valueXDiff, valueYDiff, width, height);
      }
    );
    this.showGraph = true;
  }

  setLabels(nX: number, nY: number, minX: number, minY: number, valueXDiff: number, valueYDiff: number, width: number, height: number) {
    let stepX = (this.canvasWidth - this.minHeight) / nX;
    let stepY = (this.canvasHeight - this.minWidth) / nY;

    for (let i = 1; i <= nX; i++) {
      let position = stepX * i + this.xOffset;
      let value = (stepX * i - this.minWidth) * valueXDiff / width + minX;
      this.xLabels.push({position: position, text: new Date(value).toLocaleTimeString()})
    }

    for (let i = 1; i <= nY; i++) {
      let position = this.canvasHeight - stepY * i;
      let value = (stepY * i - this.minHeight) * valueYDiff / height + minY;
      this.yLabels.push({position: position, text: Math.round(value)})
    }
  }
}
