package schleifenbauer.infrastructure.cronjob;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.service.MemoryMeasurementService;

@Singleton
public final class MemoryCollectorJob implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(MemoryCollectorJob.class.getName());

    private final MemoryMeasurementService memoryMeasurementService;
    private final IEventBus eventBus;

    @Inject
    public MemoryCollectorJob(MemoryMeasurementService memoryMeasurementService, IEventBus eventBus) {
        this.memoryMeasurementService = memoryMeasurementService;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try {
            Measurement measurement = memoryMeasurementService.measure();
            eventBus.publish(new MeasurementEvent(measurement));
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Memory collecting run failed", exception);
        }
    }
}
