package schleifenbauer.infrastructure.cronjob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.ScheduledFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollectorTaskTest {
    @Mock
    private ScheduledFuture<Object> scheduledFuture;

    @Test
    void returnsStoppedWhenTaskHasNotStarted() {
        CollectorTask task = new TestCollectorTask();

        assertEquals(CollectorTaskState.STOPPED, task.status());
    }

    @Test
    void returnsStoppedWhenScheduledFutureIsCancelled() {
        TestCollectorTask task = new TestCollectorTask();
        when(scheduledFuture.isCancelled()).thenReturn(true);
        task.setFuture(scheduledFuture);

        assertEquals(CollectorTaskState.STOPPED, task.status());
    }

    @Test
    void returnsStoppedWhenScheduledFutureIsDone() {
        TestCollectorTask task = new TestCollectorTask();
        when(scheduledFuture.isDone()).thenReturn(true);
        task.setFuture(scheduledFuture);

        assertEquals(CollectorTaskState.STOPPED, task.status());
    }

    @Test
    void returnsRunningWhenScheduledFutureIsActive() {
        TestCollectorTask task = new TestCollectorTask();
        task.setFuture(scheduledFuture);

        assertEquals(CollectorTaskState.RUNNING, task.status());
    }

    private static final class TestCollectorTask extends CollectorTask {
        @Override
        public void start() {
        }

        private void setFuture(ScheduledFuture<?> scheduledFuture) {
            setScheduledFuture(scheduledFuture);
        }
    }
}
