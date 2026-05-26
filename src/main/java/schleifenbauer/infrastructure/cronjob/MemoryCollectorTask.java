package schleifenbauer.infrastructure.cronjob;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class MemoryCollectorTask extends CollectorTask {
    private static final Logger LOGGER = Logger.getLogger(MemoryCollectorTask.class.getName());
    private static final long POLLING_INTERVAL_SECONDS = 10;

    private final ScheduledExecutorService scheduler;
    private final MemoryCollectorJob memoryCollectorJob;

    @Inject
    public MemoryCollectorTask(ScheduledExecutorService scheduler, MemoryCollectorJob memoryCollectorJob) {
        this.scheduler = scheduler;
        this.memoryCollectorJob = memoryCollectorJob;
    }

    @Override
    public void start() {
        setScheduledFuture(scheduler.scheduleAtFixedRate(memoryCollectorJob, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS));
        LOGGER.info("Scheduled memory polling every 10 seconds");
    }
}
