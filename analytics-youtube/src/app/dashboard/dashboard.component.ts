import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from 'src/app/core/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  username: string;
  numberOfVideos: number;
  videoStatistics;

  constructor(private auth: AuthService, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.auth.username$.subscribe(
      data => this.username = data
    );

    this.activatedRoute.data.subscribe(data => {
      this.videoStatistics = data.stats;
      this.numberOfVideos = data.videoNumber;
    });
  }
}
