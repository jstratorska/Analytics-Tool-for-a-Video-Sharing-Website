package edu.aubg.analyticsyoutube.model.ApiModel.VideoModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VideoSnippet {
    private String title;
    private String description;
    private String channelTitle;
    private String categoryId;
}
