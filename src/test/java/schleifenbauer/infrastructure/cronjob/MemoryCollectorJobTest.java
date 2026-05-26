package schleifenbauer.infrastructure.cronjob;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.service.MemoryMeasurementService;

@ExtendWith(MockitoExtension.class)
class MemoryCollectorJobTest {
    @Mock
    private MemoryMeasurementService memoryMeasurementService;

    @Mock
    private IEventBus eventBus;

    private static final int MEMORY_FREE_BYTES = 1024; 
    private static final String DATETIME = "2026-05-25T21:00:00";

    @Test
    void testThatPublishesMeasurementFromService() {
        Measurement measurement = new Measurement(
                MeasurementChannels.MEMORY_FREE_BYTES,
                MEMORY_FREE_BYTES,
                LocalDateTime.parse(DATETIME));
        when(memoryMeasurementService.measure()).thenReturn(measurement);

        MemoryCollectorJob job = new MemoryCollectorJob(memoryMeasurementService, eventBus);

        job.run();

        verify(eventBus).publish(new MeasurementEvent(measurement));
    }
}
