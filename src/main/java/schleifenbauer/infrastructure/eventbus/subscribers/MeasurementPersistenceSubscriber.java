package schleifenbauer.infrastructure.eventbus.subscribers;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.infrastructure.eventbus.EventBus;
import schleifenbauer.service.MeasurementService;

@Singleton
public final class MeasurementPersistenceSubscriber {
    private static final Logger LOGGER = Logger.getLogger(MeasurementPersistenceSubscriber.class.getName());

    private final EventBus eventBus;
    private final MeasurementService measurementService;

    @Inject
    public MeasurementPersistenceSubscriber(EventBus eventBus, MeasurementService measurementService) {
        this.eventBus = eventBus;
        this.measurementService = measurementService;
    }

    public void register() {
        eventBus.subscribe(Measurement.class, this::persistMeasurement);
    }

    private void persistMeasurement(Measurement measurement) {
        try {
            measurementService.save(measurement);
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, "Unable to persist measurement event", exception);
        }
    }
}
