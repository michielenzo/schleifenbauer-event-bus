package schleifenbauer.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import io.javalin.http.Context;
import schleifenbauer.infrastructure.cronjob.MemoryCollectorTask;
import schleifenbauer.infrastructure.cronjob.WeatherCollectorTask;
import schleifenbauer.rest.dto.ErrorResponseDto;
import schleifenbauer.rest.dto.StatusResponseDto;

@RequestScoped
public final class StatusController {
    private static final Logger LOGGER = Logger.getLogger(StatusController.class.getName());

    private final WeatherCollectorTask weatherCollectorTask;
    private final MemoryCollectorTask memoryCollectorTask;

    @Inject
    public StatusController(WeatherCollectorTask weatherCollectorTask, MemoryCollectorTask memoryCollectorTask) {
        this.weatherCollectorTask = weatherCollectorTask;
        this.memoryCollectorTask = memoryCollectorTask;
    }

    public void getStatus(Context context) {
        try {
            context.status(200);
            context.json(new StatusResponseDto(
                weatherCollectorTask.status().name(),
                memoryCollectorTask.status().name())
            );
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage());
            context.status(500);
            context.json(new ErrorResponseDto("An internal server error occurred."));
        }
    }
}
