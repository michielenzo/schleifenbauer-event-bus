package schleifenbauer.infrastructure.cronjob;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.service.WeatherMeasurementService;

@ExtendWith(MockitoExtension.class)
class WeatherCollectorJobTest {
    @Mock
    private WeatherMeasurementService weatherMeasurementService;

    @Mock
    private IEventBus eventBus;

    private static final String MEASUREMENT_DATETIME = "2026-05-24T00:00";
    private static final double VALUE = 42.0;

    @Test
    void publishesMeasurementsFromService() {
        Measurement temperatureMeasurement = new Measurement(
                MeasurementChannels.TEMPERATURE,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME)
        );

        Measurement humidityMeasurement = new Measurement(
                MeasurementChannels.HUMIDITY,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME)
        );

        when(weatherMeasurementService.fetchMeasurements()).thenReturn(List.of(temperatureMeasurement, humidityMeasurement));

        WeatherCollectorJob job = new WeatherCollectorJob(weatherMeasurementService, eventBus);

        job.run();

        verify(eventBus).publish(new MeasurementEvent(temperatureMeasurement));
        verify(eventBus).publish(new MeasurementEvent(humidityMeasurement));
    }
}
