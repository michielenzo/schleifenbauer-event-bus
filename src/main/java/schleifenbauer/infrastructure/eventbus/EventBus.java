package schleifenbauer.infrastructure.eventbus;

import java.util.function.Consumer;

public interface EventBus {
    <T> void publish(T event);

    <T> void subscribe(Class<T> eventType, Consumer<T> subscriber);
}
