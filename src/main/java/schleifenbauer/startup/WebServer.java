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
        app.start(7000);
    }


    private void configureEndpoints(){
        this.app.get("/api/measurements", measurementController::getLatestMeasurements);
        this.app.get("/api/status", statusController::getStatus);
    }
}
