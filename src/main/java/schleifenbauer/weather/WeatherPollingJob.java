package schleifenbauer.weather;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.eventbus.EventBus;

@Singleton
public final class WeatherPollingJob implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(WeatherPollingJob.class.getName());

    private final WeatherClient weatherClient;
    private final EventBus eventBus;

    @Inject
    public WeatherPollingJob(WeatherClient weatherClient, EventBus eventBus) {
        this.weatherClient = weatherClient;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try {
            WeatherForecastEvent forecast = weatherClient.fetchForecast();
            eventBus.publish(forecast);
            LOGGER.info("Published weather forecast event to event bus");
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Weather polling run failed", exception);
        }
    }
}
