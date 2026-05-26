package schleifenbauer.infrastructure.eventbus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;

class InMemoryMeasurementEventBusTest {
    @Test
    void publishesOnlyToSubscribersOnTheSameChannel() {
        InMemoryMeasurementEventBus eventBus = new InMemoryMeasurementEventBus();
        MeasurementEvent temperatureEvent = new MeasurementEvent(new Measurement(
                MeasurementChannels.TEMPERATURE,
                18.5,
                LocalDateTime.parse("2026-05-24T00:00")));
        List<MeasurementEvent> receivedTemperatureEvents = new ArrayList<>();
        List<MeasurementEvent> receivedHumidityEvents = new ArrayList<>();

        eventBus.subscribe(MeasurementChannels.TEMPERATURE, receivedTemperatureEvents::add);
        eventBus.subscribe(MeasurementChannels.HUMIDITY, receivedHumidityEvents::add);

        eventBus.publish(temperatureEvent);

        assertEquals(List.of(temperatureEvent), receivedTemperatureEvents);
        assertEquals(List.of(), receivedHumidityEvents);
    }
}
