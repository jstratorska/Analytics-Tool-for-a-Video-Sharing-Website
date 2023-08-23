package edu.aubg.analyticsyoutube.service;

import edu.aubg.analyticsyoutube.model.ApiModel.TrackingModel.TimeData;
import edu.aubg.analyticsyoutube.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForecastingService {
    private final TrackingRepository trackingRepository;
    private final UserService userService;

    /**
     * Returns the predicted data for a given video and statistic
     *
     * @param numberOfForecastedRecords the number of predictions
     * @param videoId                   the id of the video requested
     * @param statistic                 the statistic requested
     * @return
     */
    public List<TimeData> makeAForecast(int numberOfForecastedRecords, String videoId, String statistic) {
        String username = userService.findLoggedInUsername();
        List<TimeData> values = trackingRepository.findAll(Sort.by("id.timestamp")).stream()
                .filter(record -> record.getId().getUsername().equals(username)
                        && record.getId().getVideoId().equals(videoId)
                        && record.getId().getStatistic().equals(statistic))
                .map(record -> new TimeData(record.getId().getTimestamp(), record.getValue()))
                .collect(Collectors.toList());
        if(values.isEmpty()) {
            return values;
        }
        return doubleExponentialSmoothing(values, 0.9, 0.9, numberOfForecastedRecords);
    }

    /**
     * Defines the function for double exponential smoothing for prediction
     *
     * @param values                        the data
     * @param alpha                         the alpha parameter for the smoothing
     * @param beta                          the beta parameter for smoothing
     * @param numberOfRecordsToBeForecasted the number of predictions to be made
     * @return
     */
    private List<TimeData> doubleExponentialSmoothing(List<TimeData> values, double alpha, double beta, int numberOfRecordsToBeForecasted) {
        List<TimeData> result = new ArrayList<>();
        result.add(new TimeData(values.get(0).getTimestamp(), values.get(0).getValue()));

        double trend, level, value, lastLevel;
        String timestamp;
        level = trend = 0;

        int numberOfIterations = values.size() + numberOfRecordsToBeForecasted;
        for (int i = 1; i < numberOfIterations; i++) {
            if (i == 1) {
                level = values.get(0).getValue();
                trend = values.get(1).getValue() - values.get(0).getValue();
            }
            if (i >= values.size()) {
                value = result.get(result.size() - 1).getValue();
                timestamp = LocalDateTime.parse(result.get(result.size() - 1).getTimestamp().substring(0, 24)).plusMinutes(15).toString();
            } else {
                value = values.get(i).getValue();
                timestamp = values.get(i).getTimestamp();
            }
            lastLevel = level;
            level = alpha * value + (1 - alpha) * (level + trend);
            trend = beta * (level - lastLevel) + (1 - beta) * trend;
            result.add(new TimeData(timestamp, level + trend));
        }
        return result;
    }

}
