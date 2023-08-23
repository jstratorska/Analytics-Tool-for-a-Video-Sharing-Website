package edu.aubg.analyticsyoutube.service;

import edu.aubg.analyticsyoutube.helper.TrackingJob;
import edu.aubg.analyticsyoutube.model.TrackingSetup;
import edu.aubg.analyticsyoutube.repository.TrackingSetupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final TaskScheduler taskScheduler;
    private final TrackingService trackingService;
    private final TrackingSetupRepository trackingSetupRepository;

    private Map<TrackingSetup, ScheduledFuture<?>> jobs;

    /**
     * Retrieves tracking setup records and starts tracking jobs for them on start up
     */
    @PostConstruct()
    public void init() {
        jobs = new HashMap<>();
        List<TrackingSetup> trackingSetupList = trackingSetupRepository.findAll();
        trackingSetupList.forEach(trackingSetup -> setScheduledJob(trackingSetup.getUsername(), trackingSetup.getVideoId(), trackingSetup.getStatistic()));
    }

    /**
     * Registers and starts the tracking job for the specific video and statistic for a given user
     *
     * @param username  the username of the user
     * @param videoId   the id of the video tracked
     * @param statistic the statistic tracked
     */
    public void setScheduledJob(String username, String videoId, String statistic) {
        TrackingJob task = new TrackingJob(trackingService, username, videoId, statistic);
        ScheduledFuture<?> job = taskScheduler.scheduleWithFixedDelay(task, TimeUnit.MINUTES.toMillis(15));
        TrackingSetup trackingSetup = new TrackingSetup(username, videoId, statistic);
        jobs.put(trackingSetup, job);
    }

    /**
     * Stops the tracking job for the specific video and statistic for a given user
     *
     * @param username  the username of the user
     * @param videoId   the id of the video tracked
     * @param statistic the statistic tracked
     */
    public void stopScheduledJob(String username, String videoId, String statistic) {
        TrackingSetup trackingSetup = new TrackingSetup(username, videoId, statistic);
        ScheduledFuture<?> scheduledTask = jobs.get(trackingSetup);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobs.remove(trackingSetup);
        }
    }
}
