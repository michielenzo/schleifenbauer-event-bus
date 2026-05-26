package schleifenbauer.service;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.client.WeatherClient;
import schleifenbauer.client.dto.CurrentForecast;
import schleifenbauer.client.dto.OpenMeteoForecastResponse;
import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;

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
            new Measurement(MeasurementChannels.TEMPERATURE, current.temperature(), current.timestamp()),
            new Measurement(MeasurementChannels.HUMIDITY, current.relativeHumidity(), current.timestamp())
        );
    }
}
