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
import schleifenbauer.mapper.MeasurementMapper;
import schleifenbauer.persistence.MeasurementRepository;
import schleifenbauer.persistence.entity.MeasurementEntity;
import schleifenbauer.rest.dto.MeasurementDto;
import schleifenbauer.rest.dto.MeasurementsResponseDto;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {
    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private MeasurementMapper measurementMapper;

    @Test
    void mapsDomainMeasurementToEntityBeforeSaving() throws SQLException {
        Measurement measurement = new Measurement(
                "temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T00:00"));
        MeasurementEntity measurementEntity = new MeasurementEntity(
                null,
                "temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T00:00"));
        MeasurementService service = new MeasurementService(measurementRepository, measurementMapper);
        when(measurementMapper.toEntity(measurement)).thenReturn(measurementEntity);

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
    void mapsLatestMeasurementEntitiesToResponseDto() throws SQLException {
        MeasurementService service = new MeasurementService(measurementRepository, measurementMapper);
        MeasurementEntity measurementEntity = new MeasurementEntity(
                1L,
                "memory/usage",
                72.3,
                LocalDateTime.parse("2026-05-24T01:00:00"));
        Measurement measurement = new Measurement(
                "memory/usage",
                72.3,
                LocalDateTime.parse("2026-05-24T01:00:00"));
        MeasurementDto measurementDto = new MeasurementDto(
                "memory/usage",
                72.3,
                LocalDateTime.parse("2026-05-24T01:00:00"));
        when(measurementRepository.findLatest(50)).thenReturn(List.of(measurementEntity));
        when(measurementMapper.toDomain(measurementEntity)).thenReturn(measurement);
        when(measurementMapper.toDto(measurement)).thenReturn(measurementDto);

        MeasurementsResponseDto response = service.getLatestMeasurements();

        org.junit.jupiter.api.Assertions.assertEquals(new MeasurementsResponseDto(List.of(measurementDto)), response);
    }

    @Test
    void mapsFilteredMeasurementEntitiesToResponseDto() throws SQLException {
        MeasurementService service = new MeasurementService(measurementRepository, measurementMapper);
        MeasurementEntity measurementEntity = new MeasurementEntity(
                2L,
                "weather/temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T12:30:00"));
        Measurement measurement = new Measurement(
                "weather/temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T12:30:00"));
        MeasurementDto measurementDto = new MeasurementDto(
                "weather/temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T12:30:00"));
        when(measurementRepository.findLatestByChannel("weather/temperature", 50)).thenReturn(List.of(measurementEntity));
        when(measurementMapper.toDomain(measurementEntity)).thenReturn(measurement);
        when(measurementMapper.toDto(measurement)).thenReturn(measurementDto);

        MeasurementsResponseDto response = service.getLatestMeasurementsByChannel("weather/temperature");

        org.junit.jupiter.api.Assertions.assertEquals(new MeasurementsResponseDto(List.of(measurementDto)), response);
    }
}
