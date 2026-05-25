package schleifenbauer.weather;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class OpenMeteoWeatherClient implements WeatherClient {
    private static final URI FORECAST_URI = URI.create(
            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,relative_humidity_2m");

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Inject
    public OpenMeteoWeatherClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public WeatherForecastEvent fetchForecast() {
        HttpRequest request = HttpRequest.newBuilder(FORECAST_URI)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("Open-Meteo request failed with status " + response.statusCode());
            }

            return objectMapper.readValue(response.body(), WeatherForecastEvent.class);
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to deserialize Open-Meteo response", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Open-Meteo request was interrupted", exception);
        }
    }
}
