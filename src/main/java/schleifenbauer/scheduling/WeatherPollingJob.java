package schleifenbauer.scheduling;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.infrastructure.eventbus.MeasurementEventBus;
import schleifenbauer.service.WeatherMeasurementService;

@Singleton
public final class WeatherPollingJob implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(WeatherPollingJob.class.getName());

    private final WeatherMeasurementService weatherMeasurementService;
    private final MeasurementEventBus eventBus;

    @Inject
    public WeatherPollingJob(WeatherMeasurementService weatherMeasurementService, MeasurementEventBus eventBus) {
        this.weatherMeasurementService = weatherMeasurementService;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try {
            for (Measurement measurement : weatherMeasurementService.fetchMeasurements()) {
                eventBus.publish(new MeasurementEvent(measurement));
            }
            LOGGER.info("Published weather measurements to event bus");
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Weather polling run failed", exception);
        }
    }
}
