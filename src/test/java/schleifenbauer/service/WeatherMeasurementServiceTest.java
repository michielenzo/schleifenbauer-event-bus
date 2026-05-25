package schleifenbauer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.client.WeatherClient;
import schleifenbauer.client.dto.CurrentForecast;
import schleifenbauer.client.dto.OpenMeteoForecastResponse;
import schleifenbauer.domain.WeatherMeasurement;

@ExtendWith(MockitoExtension.class)
class WeatherMeasurementServiceTest {
    @Mock
    private WeatherClient weatherClient;

    @Test
    void convertsCurrentWeatherIntoMeasurements() {
        OpenMeteoForecastResponse forecast = new OpenMeteoForecastResponse(
                52.52,
                13.41,
                new CurrentForecast("2026-05-24T00:00", 18.5, 42.0));
        when(weatherClient.fetchCurrentWeather()).thenReturn(forecast);

        WeatherMeasurementService service = new WeatherMeasurementService(weatherClient);

        List<WeatherMeasurement> measurements = service.fetchMeasurements();

        assertEquals(
                List.of(
                        new WeatherMeasurement("temperature", 18.5, LocalDateTime.parse("2026-05-24T00:00")),
                        new WeatherMeasurement("humidity", 42.0, LocalDateTime.parse("2026-05-24T00:00"))),
                measurements);
    }
}
