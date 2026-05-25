package schleifenbauer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.domain.Measurement;
import schleifenbauer.provider.MemoryUsageProvider;

@ExtendWith(MockitoExtension.class)
class MemoryMeasurementServiceTest {
    @Mock
    private MemoryUsageProvider memoryUsageProvider;

    @Test
    void createsMeasurementFromProviderValue() {
        when(memoryUsageProvider.freeMemoryBytes()).thenReturn(1024L);
        MemoryMeasurementService service = new MemoryMeasurementService(memoryUsageProvider);

        Measurement measurement = service.measure();

        assertEquals("memory_free_bytes", measurement.channel());
        assertEquals(1024D, measurement.value());
        assertNotNull(measurement.timestamp());
    }
}
