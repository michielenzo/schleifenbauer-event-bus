package schleifenbauer.infrastructure.openmeteo;

public record OpenMeteoForecastResponse(
        double latitude,
        double longitude,
        CurrentForecast current) {
}
