package schleifenbauer.startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import schleifenbauer.rest.MeasurementController;
import schleifenbauer.rest.StatusController;

@Singleton
public final class WebServer {
    private final Javalin app;
    private final MeasurementController measurementController;
    private final StatusController statusController;

    private static final int PORT = 7000;
    private static final String MEASUREMENTS_ENDPOINT_PATH = "/api/measurements";
    private static final String STATUS_ENDPOINT_PATH = "/api/status";

    @Inject
    public WebServer(
            MeasurementController measurementController,
            StatusController statusController,
            ObjectMapper objectMapper
    ) {
        this.app = Javalin.create(config -> config.jsonMapper(new JavalinJackson(objectMapper)));
        this.measurementController = measurementController;
        this.statusController = statusController;
    
        configureEndpoints();
    }

    public void start() {
        app.start(PORT);
    }


    private void configureEndpoints(){
        this.app.get(MEASUREMENTS_ENDPOINT_PATH, measurementController::getLatestMeasurements);
        this.app.get(STATUS_ENDPOINT_PATH, statusController::getStatus);
    }
}
