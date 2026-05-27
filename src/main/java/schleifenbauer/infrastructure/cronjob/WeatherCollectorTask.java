package schleifenbauer.infrastructure.cronjob;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class WeatherCollectorTask extends CollectorTask {
    private static final Logger LOGGER = Logger.getLogger(WeatherCollectorTask.class.getName());

    private static final long INTERVAL = 30L;
    private static final long INITIAL_DELAY = 0L;

    private final ScheduledExecutorService scheduler;
    private final WeatherCollectorJob weatherCollectorJob;

    @Inject
    public WeatherCollectorTask(ScheduledExecutorService scheduler, WeatherCollectorJob weatherCollectorJob) {
        this.scheduler = scheduler;
        this.weatherCollectorJob = weatherCollectorJob;
    }

    @Override
    public void start() {
        setScheduledFuture(scheduler.scheduleAtFixedRate(weatherCollectorJob, INITIAL_DELAY, INTERVAL, TimeUnit.SECONDS));
        LOGGER.info("Scheduled weather polling every 30 seconds");
    }
}
