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
class MemoryPollingTaskTest {
    @Mock
    private ScheduledExecutorService scheduler;

    @Mock
    private MemoryPollingJob memoryPollingJob;

    @Mock
    private ScheduledFuture<Object> scheduledFuture;

    @Test
    void startsScheduledPollingJob() {
        MemoryPollingTask task = new MemoryPollingTask(scheduler, memoryPollingJob);
        when(scheduler.scheduleAtFixedRate(memoryPollingJob, 0, 10, TimeUnit.SECONDS))
                .thenAnswer(invocation -> scheduledFuture);

        task.start();

        verify(scheduler).scheduleAtFixedRate(eq(memoryPollingJob), eq(0L), eq(10L), eq(TimeUnit.SECONDS));
        assertEquals(PollingTaskState.RUNNING, task.status());
    }

    @Test
    void returnsStoppedWhenTaskHasNotStarted() {
        MemoryPollingTask task = new MemoryPollingTask(scheduler, memoryPollingJob);

        assertEquals(PollingTaskState.STOPPED, task.status());
    }

    @Test
    void returnsStoppedWhenScheduledFutureIsDone() {
        MemoryPollingTask task = new MemoryPollingTask(scheduler, memoryPollingJob);
        when(scheduler.scheduleAtFixedRate(memoryPollingJob, 0, 10, TimeUnit.SECONDS))
                .thenAnswer(invocation -> scheduledFuture);
        when(scheduledFuture.isDone()).thenReturn(true);

        task.start();

        assertEquals(PollingTaskState.STOPPED, task.status());
    }
}
