package schleifenbauer.rest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.javalin.http.Context;
import schleifenbauer.domain.Measurement;
import schleifenbauer.rest.dto.ErrorResponseDto;
import schleifenbauer.rest.dto.MeasurementDto;
import schleifenbauer.rest.dto.MeasurementsResponseDto;
import schleifenbauer.service.MeasurementService;

@ExtendWith(MockitoExtension.class)
class MeasurementControllerTest {
    @Mock
    private MeasurementService measurementService;

    @Mock
    private Context context;

    @Test
    void returnsLatestMeasurementsUsingResponseDto() throws SQLException {
        Measurement measurement = new Measurement(
                "weather/temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T12:30:00"));
        MeasurementController controller = new MeasurementController(measurementService);
        when(measurementService.getLatestMeasurements()).thenReturn(List.of(measurement));

        controller.getLatestMeasurements(context);

        verify(context).status(200);
        verify(context).json(new MeasurementsResponseDto(List.of(new MeasurementDto(
                "weather/temperature",
                18.5,
                LocalDateTime.parse("2026-05-24T12:30:00")))));
    }

    @Test
    void returnsErrorResponseDtoWhenServiceFails() throws SQLException {
        MeasurementController controller = new MeasurementController(measurementService);
        when(measurementService.getLatestMeasurements()).thenThrow(new SQLException("boom"));

        controller.getLatestMeasurements(context);

        verify(context).status(500);
        verify(context).json(new ErrorResponseDto("An internal server error occurred."));
    }
}
