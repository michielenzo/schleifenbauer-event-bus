package schleifenbauer.service;

import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.domain.Measurement;
import schleifenbauer.mapper.MeasurementMapper;
import schleifenbauer.persistence.MeasurementRepository;
import schleifenbauer.rest.dto.MeasurementDto;
import schleifenbauer.rest.dto.MeasurementsResponseDto;

@Singleton
public final class MeasurementService {
    private static final int LATEST_MEASUREMENTS_SIZE = 50;

    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper measurementMapper;

    @Inject
    public MeasurementService(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
    }

    public void save(Measurement measurement) throws SQLException {
        measurementRepository.save(measurementMapper.toEntity(measurement));
    }

    public MeasurementsResponseDto getLatestMeasurements() throws SQLException {
        List<MeasurementDto> measurements = measurementRepository.findLatest(LATEST_MEASUREMENTS_SIZE)
                .stream()
                .map(measurementMapper::toDomain)
                .map(measurementMapper::toDto)
                .toList();

        return new MeasurementsResponseDto(measurements);
    }

    public MeasurementsResponseDto getLatestMeasurementsByChannel(String channel) throws SQLException {
        List<MeasurementDto> measurements = measurementRepository.findLatestByChannel(channel, LATEST_MEASUREMENTS_SIZE)
                .stream()
                .map(measurementMapper::toDomain)
                .map(measurementMapper::toDto)
                .toList();

        return new MeasurementsResponseDto(measurements);
    }
}
