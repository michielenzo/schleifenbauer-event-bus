package schleifenbauer.startup;

import com.google.inject.Singleton;

import io.javalin.Javalin;

@Singleton
public final class WebServer {
    private final Javalin app;

    public WebServer() {
        this.app = Javalin.create();
    }

    public void start() {
        app.start(7000);
    }
}
