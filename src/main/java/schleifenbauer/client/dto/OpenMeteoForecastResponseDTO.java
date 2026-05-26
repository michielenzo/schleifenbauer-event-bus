package schleifenbauer.client.dto;

public record OpenMeteoForecastResponseDTO(
        double latitude,
        double longitude,
        CurrentForecastDTO current
) {}
