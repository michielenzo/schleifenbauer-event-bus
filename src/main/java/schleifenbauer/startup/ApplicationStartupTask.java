package schleifenbauer.startup;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.scheduling.MemoryPollingTask;
import schleifenbauer.scheduling.WeatherPollingTask;

@Singleton
public final class ApplicationStartupTask {
    private final WeatherPollingTask weatherPollingTask;
    private final MemoryPollingTask memoryPollingTask;

    @Inject
    public ApplicationStartupTask(WeatherPollingTask weatherPollingTask, MemoryPollingTask memoryPollingTask) {
        this.weatherPollingTask = weatherPollingTask;
        this.memoryPollingTask = memoryPollingTask;
    }

    public void start() {
        weatherPollingTask.start();
        memoryPollingTask.start();
    }
}
