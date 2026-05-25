package schleifenbauer.scheduling;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.infrastructure.eventbus.EventBus;
import schleifenbauer.service.MemoryMeasurementService;

@Singleton
public final class MemoryPollingJob implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(MemoryPollingJob.class.getName());

    private final MemoryMeasurementService memoryMeasurementService;
    private final EventBus eventBus;

    @Inject
    public MemoryPollingJob(MemoryMeasurementService memoryMeasurementService, EventBus eventBus) {
        this.memoryMeasurementService = memoryMeasurementService;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try {
            Measurement measurement = memoryMeasurementService.measure();
            eventBus.publish(measurement);
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Memory polling run failed", exception);
        }
    }
}
