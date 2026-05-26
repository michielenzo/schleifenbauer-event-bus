package schleifenbauer.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import io.javalin.http.Context;
import schleifenbauer.rest.dto.ErrorResponseDto;
import schleifenbauer.rest.dto.MeasurementsResponseDto;
import schleifenbauer.service.MeasurementService;

@RequestScoped
public final class MeasurementController {
    private static final Logger LOGGER = Logger.getLogger(MeasurementController.class.getName());

    private final MeasurementService measurementService;

    @Inject
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    public void getLatestMeasurements(Context context) {
        try {
            String channel = context.queryParam("channel");
            MeasurementsResponseDto response = channel == null || channel.isBlank()
                    ? measurementService.getLatestMeasurements()
                    : measurementService.getLatestMeasurementsByChannel(channel);

            context.status(200);
            context.json(response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage());
            context.status(500);
            context.json(new ErrorResponseDto("An internal server error occurred."));
        }
    }
}
