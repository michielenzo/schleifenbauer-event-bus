package schleifenbauer.infrastructure.eventbus;

public interface MeasurementEventSubscriber {
    void onMeasurementEvent(MeasurementEvent event);
}
