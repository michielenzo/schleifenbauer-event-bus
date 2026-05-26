package schleifenbauer.scheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class WeatherPollingTask extends PollingTask {
    private static final Logger LOGGER = Logger.getLogger(WeatherPollingTask.class.getName());
    private static final long POLLING_INTERVAL_SECONDS = 30;

    private final ScheduledExecutorService scheduler;
    private final WeatherPollingJob weatherPollingJob;

    @Inject
    public WeatherPollingTask(ScheduledExecutorService scheduler, WeatherPollingJob weatherPollingJob) {
        this.scheduler = scheduler;
        this.weatherPollingJob = weatherPollingJob;
    }

    @Override
    public void start() {
        setScheduledFuture(scheduler.scheduleAtFixedRate(weatherPollingJob, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS));
        LOGGER.info("Scheduled weather polling every 30 seconds");
    }
}
