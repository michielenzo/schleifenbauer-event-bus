package schleifenbauer.startup;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.infrastructure.cronjob.MemoryCollectorTask;
import schleifenbauer.infrastructure.cronjob.WeatherCollectorTask;

@Singleton
public final class ApplicationStartupTask {
    private final WeatherCollectorTask weatherCollectorTask;
    private final MemoryCollectorTask memoryCollectorTask;

    @Inject
    public ApplicationStartupTask(WeatherCollectorTask weatherCollectorTask, MemoryCollectorTask memoryCollectorTask) {
        this.weatherCollectorTask = weatherCollectorTask;
        this.memoryCollectorTask = memoryCollectorTask;
    }

    public void start() {
        weatherCollectorTask.start();
        memoryCollectorTask.start();
    }
}
