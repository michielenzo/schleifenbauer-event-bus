package schleifenbauer.startup;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.javalin.Javalin;
import schleifenbauer.rest.MeasurementController;

@Singleton
public final class WebServer {
    private final Javalin app;
    private final MeasurementController measurementController;

    @Inject
    public WebServer(MeasurementController measurementController) {
        this.app = Javalin.create();
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
