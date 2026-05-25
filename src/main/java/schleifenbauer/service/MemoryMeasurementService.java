package schleifenbauer.service;

import java.time.LocalDateTime;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.provider.MemoryUsageProvider;

@Singleton
public final class MemoryMeasurementService {
    private static final String CHANNEL = "memory_free_bytes";
    private final MemoryUsageProvider memoryUsageProvider;

    @Inject
    public MemoryMeasurementService(MemoryUsageProvider memoryUsageProvider) {
        this.memoryUsageProvider = memoryUsageProvider;
    }

    public Measurement measure() {
        return new Measurement(CHANNEL, memoryUsageProvider.freeMemoryBytes(), LocalDateTime.now());
    }
}
