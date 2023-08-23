import {Component, OnInit} from '@angular/core';
import {IPlaylist} from 'src/app/shared/playlist';
import {IVideo} from 'src/app/shared/video';
import {Constants} from 'src/app/shared/constants';
import {DataService} from '../shared/data.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-tracking',
  templateUrl: './tracking.component.html',
  styleUrls: ['./tracking.component.css']
})
export class TrackingComponent implements OnInit {
  playlists: IPlaylist[] = [];
  videos: IVideo[] = [];
  statistics: string[] = Constants.statistics;
  selectedVideoId = "";
  selectedVideoName = "";
  selectedStatistic = "";
  alreadyTracking = [];

  constructor(private dataService: DataService, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.playlists = data.playlists;
      this.videos = data.videos;
      this.alreadyTracking = data.tracking
    });
  }

  playlistSelectionChanged(event: any) {
    this.updateVideos(event.target.value);
  }

  videoSelectionUpdated(event: any) {
    this.selectedVideoId = event.target.value;
    this.selectedVideoName = event.target.options[event.target.selectedIndex].text;
  }

  statisticSelectionUpdated(event: any) {
    this.selectedStatistic = event.target.value;
  }

  updateVideos(playlistId: string) {
    this.dataService.getVideoData(playlistId).subscribe(
      (data: IVideo[]) => this.videos = data,
      (error: any) => console.log(error)
    );
  }

  addTracking() {
    if (this.selectedVideoId != "" && this.selectedStatistic != "") {
      this.dataService.setTracking(this.selectedVideoId, this.selectedStatistic).subscribe(
        data => this.alreadyTracking.push({videoId: this.selectedVideoId, videoName: this.selectedVideoName, statistic: this.selectedStatistic})
      )
    }
  }

  deleteTracking(i) {
    this.dataService.deleteTacking(this.alreadyTracking[i].videoId, this.alreadyTracking[i].statistic).subscribe(
      data => this.alreadyTracking.splice(i, 1)
    );
  }

}
