package edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeData {
    private String timestamp;
    private double value;
}
