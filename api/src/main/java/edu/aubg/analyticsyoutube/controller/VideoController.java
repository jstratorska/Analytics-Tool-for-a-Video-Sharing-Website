package edu.aubg.analyticsyoutube.controller;

import edu.aubg.analyticsyoutube.model.ApiModel.VideoCategoryModel.VideoCategory;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoCategoryModel.VideoCategoryData;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoModel.Video;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoModel.VideoData;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoModel.VideoStatistics;
import edu.aubg.analyticsyoutube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    /**
     * Returns the videos that are part of a certain playlist
     *
     * @param playlistId the id of the playlist
     * @return
     */
    @GetMapping(value = "/videos/{playlistId}")
    public Video getVideos(@PathVariable(value = "playlistId") String playlistId) {
        List<String> videoIds = videoService.getVideoIds(playlistId);
        return videoService.getVideos(videoIds);
    }

    /**
     * Returns the video id and title for all the videos part of a playlist
     *
     * @param playlistId the id of the playlist
     * @return
     */
    @GetMapping(value = "/videoData/{playlistId}")
    public List<VideoData> getVideoData(@PathVariable(value = "playlistId") String playlistId) {
        return videoService.getVideoData(playlistId);
    }

    /**
     * Returns the video id and title for all the videos part of all of the user's playlist
     *
     * @return
     */
    @GetMapping(value = "/videoData")
    public List<VideoData> getVideoData() {
        return videoService.getVideoData();
    }

    /**
     * Returns all of YouTube's categories for the current country
     *
     * @return
     */
    @GetMapping(value = "/videoCategories")
    public VideoCategory getVideoCategories() {
        return videoService.getVideoCategories();
    }

    /**
     * Returns the id and name of all of the categories for the current country
     *
     * @return
     */
    @GetMapping(value = "/videoCategoryData")
    public List<VideoCategoryData> getVideoCategoryData() {
        return videoService.getVideoCategoryData();
    }

    /**
     * Returns the aggregate statistics of all videos part of the user's playlists
     *
     * @return
     */
    @GetMapping(value = "/totalData")
    public VideoStatistics getTotalData() {
        return videoService.getTotalVideoStatisticsData();
    }

    /**
     * Returns the number of videos part of all of the user's playlist
     *
     * @return
     */
    @GetMapping(value = "/totalNumberOfVideos")
    public int getTotalNumberOfVideos() {
        return videoService.getTotalNumberOfVideos();
    }
}
