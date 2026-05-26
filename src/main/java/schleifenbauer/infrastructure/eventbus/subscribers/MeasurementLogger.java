package schleifenbauer.infrastructure.eventbus.subscribers;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.infrastructure.eventbus.MeasurementEventSubscriber;

@Singleton
public final class MeasurementLogger implements MeasurementEventSubscriber {
    private static final Logger LOGGER = Logger.getLogger(MeasurementLogger.class.getName());

    private final IEventBus eventBus;

    @Inject
    public MeasurementLogger(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void register() {
        for (String channel : MeasurementChannels.ALL) {
            eventBus.subscribe(channel, this);
        }
    }

    @Override
    public void onMeasurementEvent(MeasurementEvent event) {
        LOGGER.info(() -> "Received measurement event: " + event.measurement());
    }
}
