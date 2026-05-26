package schleifenbauer.rest.dto;

import java.time.LocalDateTime;

public record MeasurementDto(
        String channel,
        double value,
        LocalDateTime timestamp) {
}
