package schleifenbauer.infrastructure.eventbus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import com.google.inject.Singleton;

@Singleton
public final class InMemoryMeasurementEventBus implements MeasurementEventBus {
    private final Map<String, List<Consumer<MeasurementEvent>>> subscribersByChannel = new ConcurrentHashMap<>();

    @Override
    public void publish(MeasurementEvent event) {
        List<Consumer<MeasurementEvent>> subscribers =
                subscribersByChannel.getOrDefault(event.measurement().channel(), List.of());

        for (Consumer<MeasurementEvent> subscriber : subscribers) {
            subscriber.accept(event);
        }
    }

    @Override
    public void subscribe(String channel, Consumer<MeasurementEvent> subscriber) {
        subscribersByChannel
                .computeIfAbsent(channel, ignored -> new CopyOnWriteArrayList<>())
                .add(subscriber);
    }
}
