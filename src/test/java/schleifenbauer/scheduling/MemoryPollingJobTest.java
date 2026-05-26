package schleifenbauer.scheduling;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.domain.Measurement;
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.service.MemoryMeasurementService;

@ExtendWith(MockitoExtension.class)
class MemoryPollingJobTest {
    @Mock
    private MemoryMeasurementService memoryMeasurementService;

    @Mock
    private IEventBus eventBus;

    @Test
    void publishesMeasurementFromService() {
        Measurement measurement = new Measurement(
                "memory_free_bytes",
                1024,
                LocalDateTime.parse("2026-05-25T21:00:00"));
        when(memoryMeasurementService.measure()).thenReturn(measurement);

        MemoryPollingJob job = new MemoryPollingJob(memoryMeasurementService, eventBus);

        job.run();

        verify(eventBus).publish(new MeasurementEvent(measurement));
    }
}
