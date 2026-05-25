package schleifenbauer.infrastructure.eventbus.subscribers;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.WeatherMeasurement;
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
        eventBus.subscribe(WeatherMeasurement.class, this::logMeasurement);
    }

    private void logMeasurement(WeatherMeasurement measurement) {
        LOGGER.info(() -> "Received measurement event: " + measurement);
    }
}
