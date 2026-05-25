package schleifenbauer.application.weather;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.measurement.Measurement;
import schleifenbauer.infrastructure.openmeteo.CurrentForecast;
import schleifenbauer.infrastructure.openmeteo.OpenMeteoForecastResponse;

@Singleton
public final class WeatherMeasurementService {
    private final WeatherClient weatherClient;

    @Inject
    public WeatherMeasurementService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public List<Measurement> fetchMeasurements() {
        OpenMeteoForecastResponse forecast = weatherClient.fetchCurrentWeather();
        CurrentForecast current = forecast.current();

        return List.of(
                new Measurement("temperature", current.temperature(), current.timestamp()),
                new Measurement("humidity", current.relativeHumidity(), current.timestamp()));
    }
}
