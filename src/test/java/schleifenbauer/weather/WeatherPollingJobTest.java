package schleifenbauer.weather;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.eventbus.EventBus;

@ExtendWith(MockitoExtension.class)
class WeatherPollingJobTest {
    @Mock
    private WeatherClient weatherClient;

    @Mock
    private EventBus eventBus;

    @Test
    void publishesFetchedForecastEvent() {
        WeatherForecastEvent event = new WeatherForecastEvent(
                52.52,
                13.41,
                0.1,
                0,
                "GMT",
                "GMT",
                12.0,
                new HourlyUnits("iso8601", "C", "%"),
                new HourlyForecast(java.util.List.of("2026-05-24T00:00"), java.util.List.of(18.5), java.util.List.of(42)));
        when(weatherClient.fetchForecast()).thenReturn(event);

        WeatherPollingJob job = new WeatherPollingJob(weatherClient, eventBus);

        job.run();

        verify(eventBus).publish(event);
    }
}
