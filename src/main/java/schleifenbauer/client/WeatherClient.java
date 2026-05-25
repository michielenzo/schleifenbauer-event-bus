package schleifenbauer.client;

import schleifenbauer.client.dto.OpenMeteoForecastResponse;

public interface WeatherClient {
    OpenMeteoForecastResponse fetchCurrentWeather();
}
