package schleifenbauer.rest;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.javalin.http.Context;
import schleifenbauer.domain.Measurement;
import schleifenbauer.rest.dto.ErrorResponseDto;
import schleifenbauer.rest.dto.MeasurementDto;
import schleifenbauer.rest.dto.MeasurementsResponseDto;
import schleifenbauer.service.MeasurementService;

@Singleton
public final class MeasurementController {
    private final MeasurementService measurementService;

    @Inject
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    public void getLatestMeasurements(Context context) {
        try {
            List<MeasurementDto> measurements = measurementService.getLatestMeasurements()
                            .stream()
                            .map(this::toDto)
                            .toList();

            MeasurementsResponseDto response = new MeasurementsResponseDto(measurements);

            context.status(200);
            context.json(response);
        } catch (Exception exception) { 
            context.status(500);
            context.json(new ErrorResponseDto("An internal server error occurred."));
        }
    }

    private MeasurementDto toDto(Measurement measurement) {
        return new MeasurementDto(
                measurement.channel(),
                measurement.value(),
                measurement.timestamp());
    }
}
