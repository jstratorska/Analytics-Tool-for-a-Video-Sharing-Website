package edu.aubg.analyticsyoutube.repository;

import edu.aubg.analyticsyoutube.model.TrackedValue;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingRepository extends JpaRepository<TrackedValue, String> {
    List<TrackedValue> findAll(Sort sort);
}
