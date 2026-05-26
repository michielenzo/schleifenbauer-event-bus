package schleifenbauer.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    void mapsLatestMeasurementEntitiesToDomainMeasurements() throws SQLException {
        MeasurementService service = new MeasurementService(measurementRepository);
        MeasurementEntity measurementEntity = new MeasurementEntity(
                1L,
                "memory/usage",
                72.3,
                LocalDateTime.parse("2026-05-24T01:00:00"));
        when(measurementRepository.findLatest(50)).thenReturn(List.of(measurementEntity));

        List<Measurement> measurements = service.getLatestMeasurements();

        org.junit.jupiter.api.Assertions.assertEquals(List.of(new Measurement(
                "memory/usage",
                72.3,
                LocalDateTime.parse("2026-05-24T01:00:00"))), measurements);
    }
}
