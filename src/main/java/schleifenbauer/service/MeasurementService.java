package schleifenbauer.service;

import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.persistence.MeasurementRepository;
import schleifenbauer.persistence.entity.MeasurementEntity;

@Singleton
public final class MeasurementService {
    private static final int LATEST_MEASUREMENTS_SIZE = 50;

    private final MeasurementRepository measurementRepository;

    @Inject
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void save(Measurement measurement) throws SQLException {
        measurementRepository.save(toEntity(measurement));
    }

    public List<Measurement> getLatestMeasurements() throws SQLException {
        return measurementRepository.findLatest(LATEST_MEASUREMENTS_SIZE)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private MeasurementEntity toEntity(Measurement measurement) {
        return new MeasurementEntity(
                null,
                measurement.channel(),
                measurement.value(),
                measurement.timestamp());
    }

    private Measurement toDomain(MeasurementEntity measurementEntity) {
        return new Measurement(
                measurementEntity.channel(),
                measurementEntity.value(),
                measurementEntity.timestamp());
    }
}
