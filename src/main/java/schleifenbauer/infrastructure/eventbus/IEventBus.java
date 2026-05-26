package schleifenbauer.infrastructure.eventbus;

import java.util.function.Consumer;

public interface IEventBus {
    void publish(MeasurementEvent event);

    void subscribe(String channel, Consumer<MeasurementEvent> subscriber);
}
