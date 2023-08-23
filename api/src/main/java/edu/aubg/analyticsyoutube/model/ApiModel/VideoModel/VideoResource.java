package edu.aubg.analyticsyoutube.model.ApiModel.VideoModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VideoResource {
    private String id;
    private VideoSnippet snippet;
    private VideoStatistics statistics;

}
