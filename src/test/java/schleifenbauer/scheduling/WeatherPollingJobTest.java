package schleifenbauer.scheduling;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.domain.Measurement;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.infrastructure.eventbus.MeasurementEventBus;
import schleifenbauer.service.WeatherMeasurementService;

@ExtendWith(MockitoExtension.class)
class WeatherPollingJobTest {
    @Mock
    private WeatherMeasurementService weatherMeasurementService;

    @Mock
    private MeasurementEventBus eventBus;

    @Test
    void publishesMeasurementsFromService() {
        Measurement temperatureMeasurement = new Measurement(
                "temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T00:00"));
        Measurement humidityMeasurement = new Measurement(
                "humidity",
                42.0,
                LocalDateTime.parse("2026-05-24T00:00"));
        when(weatherMeasurementService.fetchMeasurements()).thenReturn(List.of(temperatureMeasurement, humidityMeasurement));

        WeatherPollingJob job = new WeatherPollingJob(weatherMeasurementService, eventBus);

        job.run();

        verify(eventBus).publish(new MeasurementEvent(temperatureMeasurement));
        verify(eventBus).publish(new MeasurementEvent(humidityMeasurement));
    }
}
