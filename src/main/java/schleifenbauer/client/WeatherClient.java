package schleifenbauer.client;

import schleifenbauer.client.dto.OpenMeteoForecastResponseDTO;

public interface WeatherClient {
    OpenMeteoForecastResponseDTO fetchCurrentWeather();
}
