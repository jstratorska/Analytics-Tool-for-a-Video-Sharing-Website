import {Component, OnInit} from '@angular/core';
import {DataService} from '../shared/data.service';
import {Constants} from '../shared/constants';
import {ITimeData} from 'src/app/shared/time-data';
import {ActivatedRoute} from '@angular/router';

@Component({
  templateUrl: './timeseries.component.html',
  styleUrls: ['./timeseries.component.css']
})
export class TimeseriesComponent implements OnInit {

  data = "";
  videos = [];
  trackings = [];
  colors: string[] = Constants.colors;
  statistics: string[];

  selectedColor;
  selectedVideoId;
  selectedStatistic;

  xPoints = [];
  yPoints = [];
  xLabels = [];
  yLabels = [];
  minWidth = 50;
  minHeight = 50;
  xOffset = 100;
  yOffset = 50;
  canvasWidth = 1000;
  canvasHeight = 400;
  showGraph = false;

  constructor(private dataService: DataService, private activatedRoute: ActivatedRoute) { };

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.trackings = data.tracking;
    });
  }

  videoSelectionUpdated(event: any) {
    this.statistics = [];
    this.selectedVideoId = event.target.value;
    this.trackings.filter(tracking => tracking.videoId == this.selectedVideoId)
      .forEach(tracking => this.statistics.push(tracking.statistic));
  }

  colorSelectionUpdated(event: any) {
    this.selectedColor = event.target.value;
  }

  statisticSelectionUpdated(event: any) {
    this.selectedStatistic = event.target.value;
  }

  generateGraph() {
    this.data = "";
    this.xPoints = [];
    this.yPoints = [];

    this.dataService.getForecastData(15, this.selectedVideoId, this.selectedStatistic).subscribe(
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

  // sets labels and the grid for the graph
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
