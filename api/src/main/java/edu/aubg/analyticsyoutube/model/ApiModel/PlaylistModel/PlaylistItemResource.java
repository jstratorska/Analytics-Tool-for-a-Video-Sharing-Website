package edu.aubg.analyticsyoutube.model.ApiModel.PlaylistModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistItemResource {
    private String id;
    private PlaylistSnippet snippet;
    private PlaylistItemContentDetails contentDetails;
}
