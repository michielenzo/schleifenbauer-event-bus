package schleifenbauer.infrastructure.eventbus.subscribers;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;

@Singleton
public final class MeasurementLogger {
    private static final Logger LOGGER = Logger.getLogger(MeasurementLogger.class.getName());

    private final IEventBus eventBus;

    @Inject
    public MeasurementLogger(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void register() {
        for (String channel : MeasurementChannels.ALL) {
            eventBus.subscribe(channel, this::logMeasurement);
        }
    }

    private void logMeasurement(MeasurementEvent event) {
        LOGGER.info(() -> "Received measurement event: " + event.measurement());
    }
}
