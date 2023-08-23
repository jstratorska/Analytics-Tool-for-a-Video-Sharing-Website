package edu.aubg.analyticsyoutube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TrackingSetup.class)
public class TrackingSetup implements Serializable {
    @Id
    private String username;
    @Id
    private String videoId;
    @Id
    private String statistic;
}
