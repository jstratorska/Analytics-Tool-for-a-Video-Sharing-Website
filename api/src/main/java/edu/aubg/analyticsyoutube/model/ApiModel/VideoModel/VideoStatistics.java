package edu.aubg.analyticsyoutube.model.ApiModel.VideoModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoStatistics {
    private double likeCount;
    private double viewCount;
    private double dislikeCount;
    private double favoriteCount;
    private double commentCount;
}
