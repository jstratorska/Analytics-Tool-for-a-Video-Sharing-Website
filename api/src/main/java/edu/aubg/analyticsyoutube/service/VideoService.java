package edu.aubg.analyticsyoutube.service;

import edu.aubg.analyticsyoutube.ApplicationProperties;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.PlaylistItem;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.PlaylistItemResource;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoCategoryModel.VideoCategory;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoCategoryModel.VideoCategoryData;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoCategoryModel.VideoCategoryResource;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoModel.Video;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoModel.VideoData;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoModel.VideoResource;
import edu.aubg.analyticsyoutube.model.ApiModel.VideoModel.VideoStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final ApplicationProperties applicationProperties;
    private final AuthorizationService authorizationService;
    private final PlaylistService playlistService;

    private HttpEntity request;
    private RestTemplate template;

    @PostConstruct
    public void init() {
        template = new RestTemplate();
    }

    /**
     * Returns a video with the given id
     *
     * @param videoId the id of the video
     * @return
     */
    public Video getVideo(String videoId) {
        Video video = exchange(applicationProperties.getBaseUrl() + "videos?id=" + videoId + "&part=snippet,contentDetails,statistics,status", new ParameterizedTypeReference<>() {
        });
        return video;
    }

    /**
     * Returns the videos with the given ids
     *
     * @param videoIds the ids of the videos
     * @return
     */
    public Video getVideos(List<String> videoIds) {
        String videoIdsString = String.join(",", videoIds.subList(0, Math.min(10, videoIds.size())));
        Video videos = exchange(applicationProperties.getBaseUrl() + "videos?id=" + videoIdsString + "&part=snippet,contentDetails,statistics,status", new ParameterizedTypeReference<>() {
        });
        return videos;
    }

    /**
     * Returns all of the video ids for videos part of a specific playlist
     *
     * @param playlistId the id of the playlist
     * @return
     */
    public List<String> getVideoIds(String playlistId) {
        List<String> videoIds = new ArrayList<>();
        PlaylistItem playlistItem = playlistService.getPlaylistItem(playlistId);
        for (PlaylistItemResource resource : playlistItem.getItems()) {
            videoIds.add(resource.getContentDetails().getVideoId());
        }
        return videoIds;
    }

    /**
     * Returns all of the video ids for videos part of all of the user's playlists
     *
     * @return
     */
    public List<String> getVideoIds() {
        List<String> videoIds = new ArrayList<>();
        List<PlaylistItem> playlistItems = playlistService.getPlaylistItems();
        playlistItems.forEach(playlistItem -> {
            for (PlaylistItemResource resource : playlistItem.getItems()) {
                videoIds.add(resource.getContentDetails().getVideoId());
            }
        });
        return videoIds;
    }

    /**
     * Returns the video id and title for all the videos part of a playlist
     *
     * @param playlistId the id of the playlist
     * @return
     */
    public List<VideoData> getVideoData(String playlistId) {
        Video video = getVideos(getVideoIds(playlistId));
        return mapVideoData(video);
    }

    /**
     * Returns the video id and title for all the videos part of all of the user's playlist
     *
     * @return
     */
    public List<VideoData> getVideoData() {
        Video video = getVideos(getVideoIds());
        return mapVideoData(video);
    }

    /**
     * Returns all of YouTube's categories for the current country
     *
     * @return
     */
    public VideoCategory getVideoCategories() {
        VideoCategory videoCategory = exchange(applicationProperties.getBaseUrl() + "videoCategories?regionCode=" + LocaleContextHolder.getLocale().getCountry() + "&part=snippet", new ParameterizedTypeReference<>() {
        });
        return videoCategory;
    }

    /**
     * Returns the id and name of all of the categories for the current country
     *
     * @return
     */
    public List<VideoCategoryData> getVideoCategoryData() {
        List<VideoCategoryData> videoCategoryData = new ArrayList<>();
        VideoCategory videoCategory = getVideoCategories();
        for (VideoCategoryResource resource : videoCategory.getItems()) {
            VideoCategoryData data = new VideoCategoryData(resource.getId(), resource.getSnippet().getTitle());
            videoCategoryData.add(data);
        }
        return videoCategoryData;
    }

    /**
     * Returns the value of a specific video statistic for a specific video
     *
     * @param videoId   the video for which the value should be returned
     * @param statistic the statistic for which the value should be returned
     * @return
     */
    public Double getVideoStatistic(String videoId, String statistic) {
        Video video = getVideo(videoId);
        VideoStatistics videoStatistics = video.getItems().get(0).getStatistics();
        switch (statistic) {
            case "likes":
                return videoStatistics.getLikeCount();
            case "views":
                return videoStatistics.getViewCount();
            case "dislikes":
                return videoStatistics.getDislikeCount();
            case "favorites":
                return videoStatistics.getFavoriteCount();
            case "comments":
                return videoStatistics.getCommentCount();
            default:
                return 0.0;
        }
    }

    /**
     * Returns the aggregate statistics of all videos part of the user's playlists
     *
     * @return
     */
    public VideoStatistics getTotalVideoStatisticsData() {
        Video video = getVideos(getVideoIds());
        double viewCount, likeCount, dislikeCount, favoriteCount, commentCount;
        viewCount = likeCount = dislikeCount = favoriteCount = commentCount = 0;

        for (VideoResource item : video.getItems()) {
            viewCount += item.getStatistics().getViewCount();
            likeCount += item.getStatistics().getLikeCount();
            dislikeCount += item.getStatistics().getDislikeCount();
            favoriteCount += item.getStatistics().getFavoriteCount();
            commentCount += item.getStatistics().getCommentCount();
        }
        return new VideoStatistics(viewCount, likeCount, dislikeCount, favoriteCount, commentCount);
    }

    /**
     * Returns the number of videos part of all of the user's playlist
     *
     * @return
     */
    public int getTotalNumberOfVideos() {
        Video video = getVideos(getVideoIds());
        return video.getItems().size();
    }

    /**
     * Helper method to map a given video object to a list of video data objects
     *
     * @param video the video object
     * @return
     */
    private List<VideoData> mapVideoData(Video video) {
        List<VideoData> videoData = new ArrayList<>();
        for (VideoResource videoResource : video.getItems()) {
            VideoStatistics statistics = videoResource.getStatistics();
            VideoData data = new VideoData(
                    videoResource.getId(),
                    videoResource.getSnippet().getTitle(),
                    statistics.getLikeCount(),
                    statistics.getViewCount(),
                    statistics.getDislikeCount(),
                    statistics.getFavoriteCount(),
                    statistics.getCommentCount(),
                    videoResource.getSnippet().getCategoryId()
            );
            videoData.add(data);
        }
        return videoData;
    }

    /**
     * Sets the needed headers and request objects for calling the YouTube API
     *
     * @param uri
     * @param responseType
     * @param <T>
     * @return
     */
    private <T> T exchange(String uri, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authorizationService.getToken());
        request = new HttpEntity(headers);
        return template.exchange(uri, HttpMethod.GET, request, responseType).getBody();
    }
}
