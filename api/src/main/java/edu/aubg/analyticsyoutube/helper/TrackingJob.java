package edu.aubg.analyticsyoutube.helper;

import edu.aubg.analyticsyoutube.service.TrackingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * A runnable object used for the scheduled tracking job for a specific track setup
 */
@AllArgsConstructor
@RequiredArgsConstructor
public class TrackingJob implements Runnable {
    private final TrackingService trackingService;
    private String username;
    private String videoId;
    private String statistic;

    @Override
    public void run() {
        trackingService.startTracking(username, videoId, statistic);
    }
}
