package schleifenbauer.infrastructure.eventbus.subscribers;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.service.MeasurementService;

@Singleton
public final class MeasurementPersistenceSubscriber {
    private static final Logger LOGGER = Logger.getLogger(MeasurementPersistenceSubscriber.class.getName());

    private final IEventBus eventBus;
    private final MeasurementService measurementService;

    @Inject
    public MeasurementPersistenceSubscriber(IEventBus eventBus, MeasurementService measurementService) {
        this.eventBus = eventBus;
        this.measurementService = measurementService;
    }

    public void register() {
        for (String channel : MeasurementChannels.ALL) {
            eventBus.subscribe(channel, this::persistMeasurement);
        }
    }

    private void persistMeasurement(MeasurementEvent event) {
        try {
            measurementService.save(event.measurement());
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, "Unable to persist measurement event", exception);
        }
    }
}
