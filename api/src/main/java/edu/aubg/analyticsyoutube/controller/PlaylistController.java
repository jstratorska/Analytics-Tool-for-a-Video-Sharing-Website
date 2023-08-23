package edu.aubg.analyticsyoutube.controller;

import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.Playlist;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.PlaylistData;
import edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel.PlaylistItem;
import edu.aubg.analyticsyoutube.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    /**
     * Returns all the available data for the user's playlists
     *
     * @return
     */
    @GetMapping(value = "/playlists")
    public Playlist getPlaylists() {
        return playlistService.getPlaylists();
    }

    /**
     * Returns the videos i.e. playlist items for all playlists
     *
     * @return
     */
    @GetMapping(value = "/playlistItems")
    public List<PlaylistItem> getPlayListItems() {
        return playlistService.getPlaylistItems();
    }

    /**
     * Returns only the title and id of all playlists
     *
     * @return
     */
    @GetMapping(value = "/playlistData")
    public List<PlaylistData> getPlaylistData() {
        return playlistService.getPlaylistData();
    }


}
