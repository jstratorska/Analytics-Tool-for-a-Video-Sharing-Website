package edu.aubg.analyticsyoutube.controller;

import edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel.TimeData;
import edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel.Tracking;
import edu.aubg.analyticsyoutube.service.SchedulerService;
import edu.aubg.analyticsyoutube.service.TrackingService;
import edu.aubg.analyticsyoutube.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final UserService userService;
    private final TrackingService trackingService;
    private final SchedulerService schedulerService;

    /**
     * Returns the data that is being tracked for a given time interval and for a given video and statistic
     *
     * @param startTime the start time of the time period
     * @param endTime   the end time of the time period
     * @param videoId   the id of the video tracked
     * @param statistic the statistic tracked
     * @return
     */
    @GetMapping(value = "/trackingData")
    public List<TimeData> getTrackedData(@RequestParam String startTime,
                                         @RequestParam String endTime,
                                         @RequestParam String videoId,
                                         @RequestParam String statistic) {
        return trackingService.getTrackedData(startTime, endTime, videoId, statistic);
    }

    /**
     * Initiates tracking for a given video and statistic
     *
     * @param videoId   the video to be tracked
     * @param statistic the statistic to be tracked
     */
    @PostMapping("/startTracking")
    public void setTracking(@RequestParam String videoId, @RequestParam String statistic) {
        trackingService.setTracking(videoId, statistic);
        schedulerService.setScheduledJob(userService.findLoggedInUsername(), videoId, statistic);
    }

    /**
     * Stops tracking the data for a given statistic and video
     *
     * @param videoId   the id of the video for which the tracking should be stopped
     * @param statistic the statistic for which the tracking should be stopped
     */
    @PostMapping("/stopTracking")
    public void stopTracking(@RequestParam String videoId, @RequestParam String statistic) {
        trackingService.stopTracking(videoId, statistic);
        schedulerService.stopScheduledJob(userService.findLoggedInUsername(), videoId, statistic);
    }

    /**
     * Returns all of the videos and statistic combinations which the user tracks
     *
     * @return
     */
    @GetMapping("/tracking")
    public List<Tracking> listTracking() {
        return trackingService.getTracking();
    }
}
