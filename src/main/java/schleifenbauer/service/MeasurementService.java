package schleifenbauer.service;

import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.persistence.MeasurementRepository;
import schleifenbauer.persistence.entity.MeasurementEntity;

@Singleton
public final class MeasurementService {
    private final MeasurementRepository measurementRepository;

    @Inject
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void save(Measurement measurement) throws SQLException {
        measurementRepository.save(toEntity(measurement));
    }

    private MeasurementEntity toEntity(Measurement measurement) {
        return new MeasurementEntity(
                null,
                measurement.channel(),
                measurement.value(),
                measurement.timestamp());
    }

    @SuppressWarnings("unused")
    private Measurement toDomain(MeasurementEntity measurementEntity) {
        return new Measurement(
                measurementEntity.channel(),
                measurementEntity.value(),
                measurementEntity.timestamp());
    }
}
