package schleifenbauer.infrastructure.eventbus;

import schleifenbauer.domain.Measurement;

public record MeasurementEvent(Measurement measurement) {
}
