package schleifenbauer.rest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.javalin.http.Context;
import schleifenbauer.rest.dto.ErrorResponseDto;
import schleifenbauer.rest.dto.StatusResponseDto;
import schleifenbauer.scheduling.MemoryCollectorTask;
import schleifenbauer.scheduling.WeatherCollectorTask;

@Singleton
public final class StatusController {
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
            context.status(500);
            context.json(new ErrorResponseDto("An internal server error occurred."));
        }
    }
}
