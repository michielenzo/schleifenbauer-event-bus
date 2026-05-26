package schleifenbauer.client.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentForecastDTO(
        String time,
        @JsonProperty("temperature_2m") double temperature,
        @JsonProperty("relative_humidity_2m") double relativeHumidity) {

    public LocalDateTime timestamp() {
        return LocalDateTime.parse(time);
    }
}
