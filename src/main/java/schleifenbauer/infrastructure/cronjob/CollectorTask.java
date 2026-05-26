package schleifenbauer.infrastructure.cronjob;

import java.util.concurrent.ScheduledFuture;

public abstract class CollectorTask {
    private ScheduledFuture<?> scheduledFuture;

    public abstract void start();

    public CollectorTaskState status() {
        return scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()
                ? CollectorTaskState.RUNNING
                : CollectorTaskState.STOPPED;
    }

    protected void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
