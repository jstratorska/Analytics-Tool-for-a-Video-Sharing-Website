package edu.aubg.analyticsyoutube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TrackedValueId implements Serializable {
    private String username;
    private String timestamp;
    private String statistic;
    private String videoId;
}
