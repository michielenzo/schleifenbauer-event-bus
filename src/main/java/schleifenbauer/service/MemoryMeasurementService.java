package schleifenbauer.service;

import java.time.LocalDateTime;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.provider.MemoryUsageProvider;

@Singleton
public final class MemoryMeasurementService {
    private final MemoryUsageProvider memoryUsageProvider;

    @Inject
    public MemoryMeasurementService(MemoryUsageProvider memoryUsageProvider) {
        this.memoryUsageProvider = memoryUsageProvider;
    }

    public Measurement measure() {
        return new Measurement(MeasurementChannels.MEMORY_FREE_BYTES, memoryUsageProvider.freeMemoryBytes(), LocalDateTime.now());
    }
}
