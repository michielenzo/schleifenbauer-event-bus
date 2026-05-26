package schleifenbauer.rest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.javalin.http.Context;
import schleifenbauer.rest.dto.ErrorResponseDto;
import schleifenbauer.rest.dto.StatusResponseDto;
import schleifenbauer.scheduling.MemoryPollingTask;
import schleifenbauer.scheduling.WeatherPollingTask;

@Singleton
public final class StatusController {
    private final WeatherPollingTask weatherPollingTask;
    private final MemoryPollingTask memoryPollingTask;

    @Inject
    public StatusController(WeatherPollingTask weatherPollingTask, MemoryPollingTask memoryPollingTask) {
        this.weatherPollingTask = weatherPollingTask;
        this.memoryPollingTask = memoryPollingTask;
    }

    public void getStatus(Context context) {
        try {
            context.status(200);
            context.json(new StatusResponseDto(
                    weatherPollingTask.status().name(),
                    memoryPollingTask.status().name())
            );
        } catch (Exception exception) {
            context.status(500);
            context.json(new ErrorResponseDto("An internal server error occurred."));
        }
    }
}
