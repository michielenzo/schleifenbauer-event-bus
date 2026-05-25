package schleifenbauer.bootstrap;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.scheduling.StartupTask;
import schleifenbauer.weather.WeatherEventLogger;
import schleifenbauer.web.WebServer;

@Singleton
public final class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private final WebServer webServer;
    private final WeatherEventLogger weatherEventLogger;
    private final StartupTask weatherPollingTask;

    @Inject
    public Application(WebServer webServer, WeatherEventLogger weatherEventLogger, StartupTask weatherPollingTask) {
        this.webServer = webServer;
        this.weatherEventLogger = weatherEventLogger;
        this.weatherPollingTask = weatherPollingTask;
    }

    public void start() {
        webServer.start();
        weatherEventLogger.register();
        weatherPollingTask.start();

        LOGGER.info("Application started");
    }
}
