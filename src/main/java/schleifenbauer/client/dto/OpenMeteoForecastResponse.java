package schleifenbauer.client.dto;

public record OpenMeteoForecastResponse(
        double latitude,
        double longitude,
        CurrentForecast current) {
}
