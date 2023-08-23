package edu.aubg.analyticsyoutube.service;

import edu.aubg.analyticsyoutube.ApplicationProperties;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.Playlist;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.PlaylistData;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.PlaylistItem;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.PlaylistResource;
import lombok.RequiredArgsConstructor;
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
public class PlaylistService {
    private final ApplicationProperties applicationProperties;
    private final AuthorizationService authorizationService;

    private HttpEntity request;
    private RestTemplate template;

    @PostConstruct
    public void init() {
        template = new RestTemplate();
    }

    /**
     * Returns all the available data for the user's playlists
     *
     * @return
     */
    public Playlist getPlaylists() {
        Playlist playlists = exchange(applicationProperties.getBaseUrl() + "playlists?mine=true&part=snippet,contentDetails,id&maxResults=10", new ParameterizedTypeReference<>() {
        });
        return playlists;
    }

    /**
     * Returns the videos i.e. playlist items for all playlists
     *
     * @return
     */
    public List<PlaylistItem> getPlaylistItems() {
        Playlist playlist = getPlaylists();
        List<PlaylistItem> items = new ArrayList<>();

        for (PlaylistResource playlistResource : playlist.getItems()) {
            PlaylistItem playlistItems = exchange(applicationProperties.getBaseUrl() + "playlistItems?playlistId=" + playlistResource.getId() + "&part=snippet,contentDetails&maxResults=50", new ParameterizedTypeReference<>() {
            });
            items.add(playlistItems);
        }
        return items;
    }

    /**
     * Returns the playlist items for a specific playlist
     *
     * @param playlistId
     * @return
     */
    public PlaylistItem getPlaylistItem(String playlistId) {
        PlaylistItem playlistItem = exchange(applicationProperties.getBaseUrl() + "playlistItems?playlistId=" + playlistId + "&part=snippet,contentDetails&maxResults=20", new ParameterizedTypeReference<>() {
        });
        return playlistItem;
    }

    /**
     * Returns only the title and id of all playlists
     *
     * @return
     */
    public List<PlaylistData> getPlaylistData() {
        List<PlaylistData> playlistData = new ArrayList<>();
        Playlist playlists = getPlaylists();
        for (PlaylistResource playlistResource : playlists.getItems()) {
            PlaylistData data = new PlaylistData(playlistResource.getId(), playlistResource.getSnippet().getTitle());
            playlistData.add(data);
        }
        return playlistData;
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
