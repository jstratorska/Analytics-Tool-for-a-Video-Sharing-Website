package edu.aubg.analyticsyoutube.model.ApiModel.VideoModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoData {
    private String id;
    private String title;
    private double likes;
    private double views;
    private double dislikes;
    private double favorites;
    private double comments;
    private String categoryId;
}
