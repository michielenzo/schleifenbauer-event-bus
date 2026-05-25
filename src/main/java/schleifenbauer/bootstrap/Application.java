package schleifenbauer.bootstrap;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.application.StartupTask;
import schleifenbauer.application.web.WebServer;
import schleifenbauer.infrastructure.logging.MeasurementLogger;

@Singleton
public final class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private final WebServer webServer;
    private final MeasurementLogger measurementLogger;
    private final StartupTask weatherPollingTask;

    @Inject
    public Application(WebServer webServer, MeasurementLogger measurementLogger, StartupTask weatherPollingTask) {
        this.webServer = webServer;
        this.measurementLogger = measurementLogger;
        this.weatherPollingTask = weatherPollingTask;
    }

    public void start() {
        webServer.start();
        measurementLogger.register();
        weatherPollingTask.start();

        LOGGER.info("Application started");
    }
}
