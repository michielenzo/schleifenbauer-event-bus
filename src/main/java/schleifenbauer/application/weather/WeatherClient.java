package schleifenbauer.application.weather;

import schleifenbauer.infrastructure.openmeteo.OpenMeteoForecastResponse;

public interface WeatherClient {
    OpenMeteoForecastResponse fetchCurrentWeather();
}
