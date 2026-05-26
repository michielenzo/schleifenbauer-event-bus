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
class MemoryCollectorTaskTest {
    @Mock
    private ScheduledExecutorService scheduler;

    @Mock
    private MemoryCollectorJob memoryCollectorJob;

    @Mock
    private ScheduledFuture<Object> scheduledFuture;

    @Test
    void startsScheduledPollingJob() {
        MemoryCollectorTask task = new MemoryCollectorTask(scheduler, memoryCollectorJob);
        when(scheduler.scheduleAtFixedRate(memoryCollectorJob, 0, 10, TimeUnit.SECONDS))
                .thenAnswer(invocation -> scheduledFuture);

        task.start();

        verify(scheduler).scheduleAtFixedRate(eq(memoryCollectorJob), eq(0L), eq(10L), eq(TimeUnit.SECONDS));
        assertEquals(CollectorTaskState.RUNNING, task.status());
    }

    @Test
    void returnsStoppedWhenTaskHasNotStarted() {
        MemoryCollectorTask task = new MemoryCollectorTask(scheduler, memoryCollectorJob);

        assertEquals(CollectorTaskState.STOPPED, task.status());
    }

    @Test
    void returnsStoppedWhenScheduledFutureIsDone() {
        MemoryCollectorTask task = new MemoryCollectorTask(scheduler, memoryCollectorJob);
        when(scheduler.scheduleAtFixedRate(memoryCollectorJob, 0, 10, TimeUnit.SECONDS))
                .thenAnswer(invocation -> scheduledFuture);
        when(scheduledFuture.isDone()).thenReturn(true);

        task.start();

        assertEquals(CollectorTaskState.STOPPED, task.status());
    }
}
