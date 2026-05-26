package schleifenbauer.infrastructure.eventbus.subscribers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.infrastructure.eventbus.MeasurementEvent;
import schleifenbauer.infrastructure.eventbus.MeasurementEventBus;
import schleifenbauer.service.MeasurementService;

@ExtendWith(MockitoExtension.class)
class MeasurementPersistenceSubscriberTest {
    @Mock
    private MeasurementEventBus eventBus;

    @Mock
    private MeasurementService measurementService;

    @Test
    void registersMeasurementSubscription() {
        MeasurementPersistenceSubscriber subscriber = new MeasurementPersistenceSubscriber(eventBus, measurementService);

        subscriber.register();

        verify(eventBus).subscribe(org.mockito.ArgumentMatchers.eq(MeasurementChannels.TEMPERATURE), org.mockito.ArgumentMatchers.any());
        verify(eventBus).subscribe(org.mockito.ArgumentMatchers.eq(MeasurementChannels.HUMIDITY), org.mockito.ArgumentMatchers.any());
        verify(eventBus).subscribe(org.mockito.ArgumentMatchers.eq(MeasurementChannels.MEMORY_FREE_BYTES), org.mockito.ArgumentMatchers.any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void logsAndContinuesWhenPersistenceFails() throws SQLException {
        Measurement measurement = new Measurement(
                "temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T00:00"));
        MeasurementPersistenceSubscriber subscriber = new MeasurementPersistenceSubscriber(eventBus, measurementService);
        MeasurementEvent event = new MeasurementEvent(measurement);
        doThrow(new SQLException("boom")).when(measurementService).save(measurement);

        subscriber.register();

        org.mockito.ArgumentCaptor<Consumer<MeasurementEvent>> captor =
                org.mockito.ArgumentCaptor.forClass((Class<Consumer<MeasurementEvent>>) (Class<?>) Consumer.class);
        verify(eventBus).subscribe(org.mockito.ArgumentMatchers.eq(MeasurementChannels.TEMPERATURE), captor.capture());

        captor.getValue().accept(event);

        verify(measurementService).save(measurement);
    }
}
