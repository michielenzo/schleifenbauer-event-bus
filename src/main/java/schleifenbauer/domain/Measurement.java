package schleifenbauer.domain;

import java.time.LocalDateTime;

public record Measurement(
        String channel,
        double value,
        LocalDateTime timestamp) {
}
