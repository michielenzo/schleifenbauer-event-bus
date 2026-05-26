package schleifenbauer.infrastructure.eventbus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.inject.Singleton;

@Singleton
public final class InMemoryEventBus implements IEventBus {
    private final Map<String, List<MeasurementEventSubscriber>> subscribersByChannel = new ConcurrentHashMap<>();

    @Override
    public void publish(MeasurementEvent event) {
        List<MeasurementEventSubscriber> subscribers =
                subscribersByChannel.getOrDefault(event.measurement().channel(), List.of());

        for (MeasurementEventSubscriber subscriber : subscribers) {
            subscriber.onMeasurementEvent(event);
        }
    }

    @Override
    public void subscribe(String channel, MeasurementEventSubscriber subscriber) {
        subscribersByChannel
                .computeIfAbsent(channel, ignored -> new CopyOnWriteArrayList<>())
                .add(subscriber);
    }
}
