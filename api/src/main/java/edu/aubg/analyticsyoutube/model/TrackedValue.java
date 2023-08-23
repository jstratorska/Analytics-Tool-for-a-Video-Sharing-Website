package edu.aubg.analyticsyoutube.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TrackedValue {
    @EmbeddedId
    private TrackedValueId id;

    private Double value;


}
