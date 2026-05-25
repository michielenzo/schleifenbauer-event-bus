package schleifenbauer.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HourlyUnits(
        String time,
        @JsonProperty("temperature_2m") String temperature,
        @JsonProperty("relative_humidity_2m") String relativeHumidity) {
}
