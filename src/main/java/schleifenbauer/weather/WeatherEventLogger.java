package schleifenbauer.weather;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.eventbus.EventBus;

@Singleton
public final class WeatherEventLogger {
    private static final Logger LOGGER = Logger.getLogger(WeatherEventLogger.class.getName());

    private final EventBus eventBus;

    @Inject
    public WeatherEventLogger(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void register() {
        eventBus.subscribe(WeatherForecastEvent.class, this::logEvent);
    }

    private void logEvent(WeatherForecastEvent event) {
        LOGGER.info(() -> "Received weather event: " + event);
    }
}
