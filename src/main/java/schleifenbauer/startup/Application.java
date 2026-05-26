package schleifenbauer.startup;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.infrastructure.cronjob.MemoryCollectorTask;
import schleifenbauer.infrastructure.cronjob.WeatherCollectorTask;
import schleifenbauer.infrastructure.eventbus.subscribers.MeasurementLogger;
import schleifenbauer.infrastructure.eventbus.subscribers.MeasurementPersistenceSubscriber;

@Singleton
public final class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private final WebServer webServer;
    private final MeasurementLogger measurementLogger;
    private final MeasurementPersistenceSubscriber measurementPersistenceSubscriber;
    private final WeatherCollectorTask weatherCollectorTask;
    private final MemoryCollectorTask memoryCollectorTask;

    @Inject
    public Application(
        WebServer webServer,
        MeasurementLogger measurementLogger,
        MeasurementPersistenceSubscriber measurementPersistenceSubscriber,
        WeatherCollectorTask weatherCollectorTask,
        MemoryCollectorTask memoryCollectorTask
    ) {
        this.webServer = webServer;
        this.measurementLogger = measurementLogger;
        this.measurementPersistenceSubscriber = measurementPersistenceSubscriber;
        this.weatherCollectorTask = weatherCollectorTask;
        this.memoryCollectorTask = memoryCollectorTask;
    }

    public void start() {
        webServer.start();
        measurementLogger.register();
        measurementPersistenceSubscriber.register();
        weatherCollectorTask.start();
        memoryCollectorTask.start();

        LOGGER.info("Application started");
    }
}
