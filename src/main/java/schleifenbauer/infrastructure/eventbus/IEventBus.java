package schleifenbauer.infrastructure.eventbus;

public interface IEventBus {
    void publish(MeasurementEvent event);

    void subscribe(String channel, MeasurementEventSubscriber subscriber);
}
