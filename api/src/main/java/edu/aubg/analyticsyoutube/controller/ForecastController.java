package edu.aubg.analyticsyoutube.controller;

import edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel.TimeData;
import edu.aubg.analyticsyoutube.service.ForecastingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ForecastController {

    private final ForecastingService forecastingService;

    /**
     * Returns the predicted data for a given video and statistic
     *
     * @param predictions the number of predictions
     * @param videoId     the id of the video requested
     * @param statistic   the statistic requested
     * @return
     */
    @GetMapping(value = "/forecast")
    public List<TimeData> getForecast(@RequestParam int predictions, @RequestParam String videoId, @RequestParam String statistic) {
        return forecastingService.makeAForecast(predictions, videoId, statistic);
    }
}
