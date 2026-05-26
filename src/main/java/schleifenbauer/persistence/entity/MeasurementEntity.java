package schleifenbauer.persistence.entity;

import java.time.LocalDateTime;

public record MeasurementEntity(
        Long id,
        String channel,
        double value,
        LocalDateTime timestamp
) {}
