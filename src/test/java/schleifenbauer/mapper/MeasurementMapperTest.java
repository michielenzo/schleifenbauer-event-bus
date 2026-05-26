package schleifenbauer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import schleifenbauer.domain.Measurement;
import schleifenbauer.persistence.entity.MeasurementEntity;

class MeasurementMapperTest {
    @Test
    void mapsDomainMeasurementToEntity() {
        MeasurementMapper mapper = new MeasurementMapper();
        Measurement measurement = new Measurement(
                "temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T00:00"));

        MeasurementEntity measurementEntity = mapper.toEntity(measurement);

        assertNull(measurementEntity.id());
        assertEquals("temperature", measurementEntity.channel());
        assertEquals(18.5, measurementEntity.value());
        assertEquals(LocalDateTime.parse("2026-05-24T00:00"), measurementEntity.timestamp());
    }

    @Test
    void mapsEntityToDomainMeasurement() {
        MeasurementMapper mapper = new MeasurementMapper();
        MeasurementEntity measurementEntity = new MeasurementEntity(
                1L,
                "memory/usage",
                72.3,
                LocalDateTime.parse("2026-05-24T01:00:00"));

        Measurement measurement = mapper.toDomain(measurementEntity);

        assertEquals(new Measurement(
                "memory/usage",
                72.3,
                LocalDateTime.parse("2026-05-24T01:00:00")), measurement);
    }
}
