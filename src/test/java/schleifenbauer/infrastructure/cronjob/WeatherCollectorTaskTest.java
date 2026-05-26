package schleifenbauer.infrastructure.cronjob;

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
class WeatherCollectorTaskTest {
    @Mock
    private ScheduledExecutorService scheduler;

    @Mock
    private WeatherCollectorJob weatherCollectorJob;

    @Mock
    private ScheduledFuture<Object> scheduledFuture;

    @Test
    void returnsStoppedWhenTaskHasNotStarted() {
        WeatherCollectorTask task = new WeatherCollectorTask(scheduler, weatherCollectorJob);

        assertEquals(CollectorTaskState.STOPPED, task.status());
    }

    @Test
    void returnsStoppedWhenScheduledFutureIsCancelled() {
        WeatherCollectorTask task = new WeatherCollectorTask(scheduler, weatherCollectorJob);
        when(scheduler.scheduleAtFixedRate(weatherCollectorJob, 0, 30, TimeUnit.SECONDS))
                .thenAnswer(invocation -> scheduledFuture);
        when(scheduledFuture.isCancelled()).thenReturn(true);

        task.start();

        assertEquals(CollectorTaskState.STOPPED, task.status());
    }
}
