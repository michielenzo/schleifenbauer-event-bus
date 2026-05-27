package schleifenbauer.rest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.javalin.http.Context;
import schleifenbauer.rest.dto.ErrorResponseDto;
import schleifenbauer.rest.dto.StatusResponseDto;
import schleifenbauer.infrastructure.cronjob.CollectorTaskState;
import schleifenbauer.infrastructure.cronjob.MemoryCollectorTask;
import schleifenbauer.infrastructure.cronjob.WeatherCollectorTask;

@ExtendWith(MockitoExtension.class)
class StatusControllerTest {
    @Mock
    private WeatherCollectorTask weatherCollectorTask;

    @Mock
    private MemoryCollectorTask memoryCollectorTask;

    @Mock
    private Context context;

    @Test
    void returnsStatusForEachPollingJob() {
        StatusController controller = new StatusController(weatherCollectorTask, memoryCollectorTask);
        when(weatherCollectorTask.status()).thenReturn(CollectorTaskState.RUNNING);
        when(memoryCollectorTask.status()).thenReturn(CollectorTaskState.STOPPED);

        controller.getStatus(context);

        verify(context).status(200);
        verify(context).json(new StatusResponseDto(CollectorTaskState.RUNNING.name(), CollectorTaskState.STOPPED.name()));
    }

    @Test
    void returnsErrorResponseDtoWhenStatusFails() {
        StatusController controller = new StatusController(weatherCollectorTask, memoryCollectorTask);
        when(weatherCollectorTask.status()).thenThrow(new IllegalStateException("boom"));

        controller.getStatus(context);

        verify(context).status(500);
        verify(context).json(new ErrorResponseDto("An internal server error occurred."));
    }
}
