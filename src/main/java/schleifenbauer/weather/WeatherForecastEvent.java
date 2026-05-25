package schleifenbauer.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherForecastEvent(
        double latitude,
        double longitude,
        @JsonProperty("generationtime_ms") double generationTimeMs,
        @JsonProperty("utc_offset_seconds") int utcOffsetSeconds,
        String timezone,
        @JsonProperty("timezone_abbreviation") String timezoneAbbreviation,
        double elevation,
        @JsonProperty("hourly_units") HourlyUnits hourlyUnits,
        HourlyForecast hourly) {
}
