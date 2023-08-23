package edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tracking {
    private String videoId;
    private String videoName;
    private String statistic;
}
