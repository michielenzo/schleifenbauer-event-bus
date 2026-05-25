package schleifenbauer.startup;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.infrastructure.eventbus.subscribers.MeasurementLogger;
import schleifenbauer.infrastructure.eventbus.subscribers.MeasurementPersistenceSubscriber;

@Singleton
public final class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private final WebServer webServer;
    private final MeasurementLogger measurementLogger;
    private final MeasurementPersistenceSubscriber measurementPersistenceSubscriber;
    private final StartupTask weatherPollingTask;

    @Inject
    public Application(
        WebServer webServer,
        MeasurementLogger measurementLogger,
        MeasurementPersistenceSubscriber measurementPersistenceSubscriber,
        StartupTask weatherPollingTask
    ) {
        this.webServer = webServer;
        this.measurementLogger = measurementLogger;
        this.measurementPersistenceSubscriber = measurementPersistenceSubscriber;
        this.weatherPollingTask = weatherPollingTask;
    }

    public void start() {
        webServer.start();
        measurementLogger.register();
        measurementPersistenceSubscriber.register();
        weatherPollingTask.start();

        LOGGER.info("Application started");
    }
}
