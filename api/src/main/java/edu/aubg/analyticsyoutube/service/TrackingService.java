package edu.aubg.analyticsyoutube.service;

import edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel.TimeData;
import edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel.Tracking;
import edu.aubg.analyticsyoutube.model.TrackedValue;
import edu.aubg.analyticsyoutube.model.TrackedValueId;
import edu.aubg.analyticsyoutube.model.TrackingSetup;
import edu.aubg.analyticsyoutube.repository.TrackingRepository;
import edu.aubg.analyticsyoutube.repository.TrackingSetupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;
    private final TrackingSetupRepository trackingSetupRepository;
    private final VideoService videoService;
    private final UserService userService;

    /**
     * Returns the data that is being tracked for a given time interval and for a given video and statistic
     *
     * @param startTime the start time of the time period
     * @param endTime   the end time of the time period
     * @param videoId   the id of the video tracked
     * @param statistic the statistic tracked
     * @return
     */
    public List<TimeData> getTrackedData(String startTime, String endTime, String videoId, String statistic) {
        List<TrackedValue> trackedValues = trackingRepository.findAll();
        List<TimeData> timeDataList = trackedValues.stream()
                .filter(trackedValue -> (trackedValue.getId().getVideoId().equals(videoId))
                        && (trackedValue.getId().getUsername().equals(userService.findLoggedInUsername()))
                        && (trackedValue.getId().getStatistic().equals(statistic))
                        && (trackedValue.getId().getTimestamp().compareTo(startTime) >= 0)
                        && (trackedValue.getId().getTimestamp().compareTo(endTime) <= 0))
                .map(trackedValue -> new TimeData(trackedValue.getId().getTimestamp(), trackedValue.getValue()))
                .collect(Collectors.toList());
        return timeDataList;
    }

    /**
     * Initiates tracking for a given video and statistic
     *
     * @param videoId   the video to be tracked
     * @param statistic the statistic to be tracked
     */
    public void setTracking(String videoId, String statistic) {
        TrackingSetup trackingSetup = new TrackingSetup(userService.findLoggedInUsername(), videoId, statistic);
        trackingSetupRepository.save(trackingSetup);
    }

    /**
     * Obtains and records the tracked data
     *
     * @param username  the username of the user who tracks the video and the statistic
     * @param videoId   the id of the video tracked
     * @param statistic the statistic tracked
     * @return
     */
    public boolean startTracking(String username, String videoId, String statistic) {
        TrackedValueId id = new TrackedValueId(username, Instant.now().toString(), statistic, videoId);
        TrackedValue record = new TrackedValue(id, videoService.getVideoStatistic(videoId, statistic));
        trackingRepository.save(record);
        return true;
    }

    /**
     * Stops tracking the data for a given statistic and video
     *
     * @param videoId   the id of the video for which the tracking should be stopped
     * @param statistic the statistic for which the tracking should be stopped
     */
    public void stopTracking(String videoId, String statistic) {
        TrackingSetup trackingSetup = new TrackingSetup(userService.findLoggedInUsername(), videoId, statistic);
        trackingSetupRepository.delete(trackingSetup);
    }

    /**
     * Returns all of the videos and statistic combinations which the user tracks
     *
     * @return
     */
    public List<Tracking> getTracking() {
        List<TrackingSetup> trackingSetups = trackingSetupRepository.findAll();
        List<Tracking> trackingList = trackingSetups.stream()
                .filter(trackedSetup -> (trackedSetup.getUsername().equals(userService.findLoggedInUsername())))
                .map(trackingSetup -> {
                    String videoName = videoService.getVideo(trackingSetup.getVideoId()).getItems().get(0).getSnippet().getTitle();
                    return new Tracking(trackingSetup.getVideoId(), videoName, trackingSetup.getStatistic());
                })
                .collect(Collectors.toList());
        return trackingList;
    }

}
