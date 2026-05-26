package schleifenbauer.mapper;

import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.persistence.entity.MeasurementEntity;
import schleifenbauer.rest.dto.MeasurementDto;

@Singleton
public final class MeasurementMapper {
    public MeasurementEntity toEntity(Measurement measurement) {
        return new MeasurementEntity(
                null,
                measurement.channel(),
                measurement.value(),
                measurement.timestamp());
    }

    public Measurement toDomain(MeasurementEntity measurementEntity) {
        return new Measurement(
                measurementEntity.channel(),
                measurementEntity.value(),
                measurementEntity.timestamp());
    }

    public MeasurementDto toDto(Measurement measurement) {
        return new MeasurementDto(
                measurement.channel(),
                measurement.value(),
                measurement.timestamp());
    }
}
