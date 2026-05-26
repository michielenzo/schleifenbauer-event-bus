package schleifenbauer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import schleifenbauer.domain.Measurement;
import schleifenbauer.domain.MeasurementChannels;
import schleifenbauer.persistence.entity.MeasurementEntity;
import schleifenbauer.rest.dto.MeasurementDto;

class MeasurementMapperTest {

    private static final double VALUE = 72;    
    private static final long ENTITY_ID = 1l;
    private static final String MEASUREMENT_DATETIME = "2026-05-24T00:00";
    
    private MeasurementMapper mapper;    

    @BeforeEach
    @SuppressWarnings("unused")
    void setup(){
        mapper = new MeasurementMapper();
    }

    @Test
    void mapsDomainMeasurementToEntity() {
        Measurement measurement = new Measurement(
                MeasurementChannels.TEMPERATURE,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME)
        );

        MeasurementEntity measurementEntity = mapper.toEntity(measurement);
                
        assertNull(measurementEntity.id());
        assertEquals(MeasurementChannels.TEMPERATURE, measurementEntity.channel());
        assertEquals(VALUE, measurementEntity.value());
        assertEquals(LocalDateTime.parse(MEASUREMENT_DATETIME), measurementEntity.timestamp());
    }

    @Test
    void mapsEntityToDomainMeasurement() {
        MeasurementEntity measurementEntity = new MeasurementEntity(
                ENTITY_ID,
                MeasurementChannels.MEMORY_FREE_BYTES,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME)
        );

        Measurement measurement = mapper.toDomain(measurementEntity);

        assertEquals(new Measurement(
                MeasurementChannels.MEMORY_FREE_BYTES,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME)
        ), measurement);
    }

    @Test
    void mapsDomainMeasurementToDto() {
        Measurement measurement = new Measurement(
                MeasurementChannels.TEMPERATURE,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME)
        );

        MeasurementDto measurementDto = mapper.toDto(measurement);

        assertEquals(new MeasurementDto(
                MeasurementChannels.TEMPERATURE,
                VALUE,
                LocalDateTime.parse(MEASUREMENT_DATETIME)
        ), measurementDto);
    }
}
