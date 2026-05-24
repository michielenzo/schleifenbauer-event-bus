package schleifenbauer;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, world!");

        var app = Javalin.create().start(7000);

        app.get("/", ctx -> ctx.result("Hello World"));
        
        var scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Running every 2 seconds");
        }, 0, 2, TimeUnit.SECONDS);
    }
}
