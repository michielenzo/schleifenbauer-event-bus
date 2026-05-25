package schleifenbauer.eventbus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.inject.Singleton;

@Singleton
public final class InMemoryEventBus implements EventBus {
    private final Map<Class<?>, List<Consumer<?>>> subscribers = new LinkedHashMap<>();

    @Override
    public <T> void publish(T event) {
        List<Consumer<?>> handlers = subscribers.getOrDefault(event.getClass(), List.of());

        for (Consumer<?> handler : handlers) {
            @SuppressWarnings("unchecked")
            Consumer<T> typedHandler = (Consumer<T>) handler;
            typedHandler.accept(event);
        }
    }

    @Override
    public <T> void subscribe(Class<T> eventType, Consumer<T> subscriber) {
        subscribers
                .computeIfAbsent(eventType, ignored -> new ArrayList<>())
                .add(subscriber);
    }
}
