package schleifenbauer.weather;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HourlyForecast(
        List<String> time,
        @JsonProperty("temperature_2m") List<Double> temperatures,
        @JsonProperty("relative_humidity_2m") List<Integer> relativeHumidities) {
}
