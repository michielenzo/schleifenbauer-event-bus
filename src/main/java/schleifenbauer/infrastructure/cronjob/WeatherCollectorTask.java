package schleifenbauer.infrastructure.cronjob;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class WeatherCollectorTask extends CollectorTask {
    private static final Logger LOGGER = Logger.getLogger(WeatherCollectorTask.class.getName());
    private static final long POLLING_INTERVAL_SECONDS = 30;

    private final ScheduledExecutorService scheduler;
    private final WeatherCollectorJob weatherCollectorJob;

    @Inject
    public WeatherCollectorTask(ScheduledExecutorService scheduler, WeatherCollectorJob weatherCollectorJob) {
        this.scheduler = scheduler;
        this.weatherCollectorJob = weatherCollectorJob;
    }

    @Override
    public void start() {
        setScheduledFuture(scheduler.scheduleAtFixedRate(weatherCollectorJob, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS));
        LOGGER.info("Scheduled weather polling every 30 seconds");
    }
}
