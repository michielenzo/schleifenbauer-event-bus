package schleifenbauer.scheduling;

import java.util.concurrent.ScheduledFuture;

public abstract class PollingTask {
    private ScheduledFuture<?> scheduledFuture;

    public abstract void start();

    public PollingTaskState status() {
        return scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()
                ? PollingTaskState.RUNNING
                : PollingTaskState.STOPPED;
    }

    protected void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
