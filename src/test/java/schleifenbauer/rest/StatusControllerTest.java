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
import schleifenbauer.scheduling.MemoryPollingTask;
import schleifenbauer.scheduling.PollingTaskState;
import schleifenbauer.scheduling.WeatherPollingTask;

@ExtendWith(MockitoExtension.class)
class StatusControllerTest {
    @Mock
    private WeatherPollingTask weatherPollingTask;

    @Mock
    private MemoryPollingTask memoryPollingTask;

    @Mock
    private Context context;

    @Test
    void returnsStatusForEachPollingJob() {
        StatusController controller = new StatusController(weatherPollingTask, memoryPollingTask);
        when(weatherPollingTask.status()).thenReturn(PollingTaskState.RUNNING);
        when(memoryPollingTask.status()).thenReturn(PollingTaskState.STOPPED);

        controller.getStatus(context);

        verify(context).status(200);
        verify(context).json(new StatusResponseDto("RUNNING", "STOPPED"));
    }

    @Test
    void returnsErrorResponseDtoWhenStatusFails() {
        StatusController controller = new StatusController(weatherPollingTask, memoryPollingTask);
        when(weatherPollingTask.status()).thenThrow(new IllegalStateException("boom"));

        controller.getStatus(context);

        verify(context).status(500);
        verify(context).json(new ErrorResponseDto("An internal server error occurred."));
    }
}
