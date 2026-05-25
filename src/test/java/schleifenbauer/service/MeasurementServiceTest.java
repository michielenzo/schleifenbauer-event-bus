package schleifenbauer.service;

import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schleifenbauer.domain.Measurement;
import schleifenbauer.persistence.MeasurementRepository;
import schleifenbauer.persistence.entity.MeasurementEntity;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {
    @Mock
    private MeasurementRepository measurementRepository;

    @Test
    void mapsDomainMeasurementToEntityBeforeSaving() throws SQLException {
        Measurement measurement = new Measurement(
                "temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T00:00"));
        MeasurementService service = new MeasurementService(measurementRepository);

        service.save(measurement);

        ArgumentCaptor<MeasurementEntity> measurementEntityCaptor = ArgumentCaptor.forClass(MeasurementEntity.class);
        verify(measurementRepository).save(measurementEntityCaptor.capture());

        MeasurementEntity savedEntity = measurementEntityCaptor.getValue();
        org.junit.jupiter.api.Assertions.assertNull(savedEntity.id());
        org.junit.jupiter.api.Assertions.assertEquals("temperature", savedEntity.channel());
        org.junit.jupiter.api.Assertions.assertEquals(18.5, savedEntity.value());
        org.junit.jupiter.api.Assertions.assertEquals(LocalDateTime.parse("2026-05-24T00:00"), savedEntity.timestamp());
    }
}
