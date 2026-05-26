package schleifenbauer.infrastructure.eventbus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;

class InMemoryMeasurementEventBusTest {

    private static final String MEASUREMENT_DATETIME = "2026-05-24T00:00";
    private static final double VALUE = 18.5;

    @Test
    void publishesOnlyToSubscribersOnTheSameChannel() {
        InMemoryEventBus eventBus = new InMemoryEventBus();
        MeasurementEvent temperatureEvent = new MeasurementEvent(new Measurement(
                MeasurementChannels.TEMPERATURE,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME))
        );
                
        List<MeasurementEvent> receivedTemperatureEvents = new ArrayList<>();
        List<MeasurementEvent> receivedHumidityEvents = new ArrayList<>();

        eventBus.subscribe(MeasurementChannels.TEMPERATURE, receivedTemperatureEvents::add);
        eventBus.subscribe(MeasurementChannels.HUMIDITY, receivedHumidityEvents::add);

        eventBus.publish(temperatureEvent);

        assertEquals(List.of(temperatureEvent), receivedTemperatureEvents);
        assertEquals(List.of(), receivedHumidityEvents);
    }
}
