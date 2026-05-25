package schleifenbauer.infrastructure.logging;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.measurement.Measurement;
import schleifenbauer.infrastructure.eventbus.EventBus;

@Singleton
public final class MeasurementLogger {
    private static final Logger LOGGER = Logger.getLogger(MeasurementLogger.class.getName());

    private final EventBus eventBus;

    @Inject
    public MeasurementLogger(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void register() {
        eventBus.subscribe(Measurement.class, this::logMeasurement);
    }

    private void logMeasurement(Measurement measurement) {
        LOGGER.info(() -> "Received measurement event: " + measurement);
    }
}
