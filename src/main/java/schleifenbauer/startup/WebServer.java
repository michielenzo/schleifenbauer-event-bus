package schleifenbauer.startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import schleifenbauer.rest.MeasurementController;

@Singleton
public final class WebServer {
    private final Javalin app;
    private final MeasurementController measurementController;

    @Inject
    public WebServer(MeasurementController measurementController, ObjectMapper objectMapper) {
        this.app = Javalin.create(config -> config.jsonMapper(new JavalinJackson(objectMapper)));
        this.measurementController = measurementController;
    
        configureEndpoints();
    }

    public void start() {
        app.start(7000);
    }


    private void configureEndpoints(){
        this.app.get("/api/measurements", measurementController::getLatestMeasurements);
    }
}
