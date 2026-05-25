package schleifenbauer.domain;

import java.time.LocalDateTime;

public record WeatherMeasurement(
        String channel,
        double value,
        LocalDateTime timestamp) {
}
