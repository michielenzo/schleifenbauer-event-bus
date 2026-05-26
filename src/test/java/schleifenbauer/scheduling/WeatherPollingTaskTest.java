package schleifenbauer.scheduling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherPollingTaskTest {
    @Mock
    private ScheduledExecutorService scheduler;

    @Mock
    private WeatherPollingJob weatherPollingJob;

    @Mock
    private ScheduledFuture<Object> scheduledFuture;

    @Test
    void startsScheduledPollingJob() {
        WeatherPollingTask task = new WeatherPollingTask(scheduler, weatherPollingJob);
        when(scheduler.scheduleAtFixedRate(weatherPollingJob, 0, 30, TimeUnit.SECONDS))
                .thenAnswer(invocation -> scheduledFuture);

        task.start();

        verify(scheduler).scheduleAtFixedRate(eq(weatherPollingJob), eq(0L), eq(30L), eq(TimeUnit.SECONDS));
        assertEquals(PollingTaskState.RUNNING, task.status());
    }

    @Test
    void returnsStoppedWhenTaskHasNotStarted() {
        WeatherPollingTask task = new WeatherPollingTask(scheduler, weatherPollingJob);

        assertEquals(PollingTaskState.STOPPED, task.status());
    }

    @Test
    void returnsStoppedWhenScheduledFutureIsCancelled() {
        WeatherPollingTask task = new WeatherPollingTask(scheduler, weatherPollingJob);
        when(scheduler.scheduleAtFixedRate(weatherPollingJob, 0, 30, TimeUnit.SECONDS))
                .thenAnswer(invocation -> scheduledFuture);
        when(scheduledFuture.isCancelled()).thenReturn(true);

        task.start();

        assertEquals(PollingTaskState.STOPPED, task.status());
    }
}
