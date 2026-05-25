package schleifenbauer.service;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.client.WeatherClient;
import schleifenbauer.client.dto.CurrentForecast;
import schleifenbauer.client.dto.OpenMeteoForecastResponse;
import schleifenbauer.domain.WeatherMeasurement;

@Singleton
public final class WeatherMeasurementService {
    private final WeatherClient weatherClient;

    @Inject
    public WeatherMeasurementService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public List<WeatherMeasurement> fetchMeasurements() {
        OpenMeteoForecastResponse forecast = weatherClient.fetchCurrentWeather();
        CurrentForecast current = forecast.current();

        return List.of(
                new WeatherMeasurement("temperature", current.temperature(), current.timestamp()),
                new WeatherMeasurement("humidity", current.relativeHumidity(), current.timestamp()));
    }
}
