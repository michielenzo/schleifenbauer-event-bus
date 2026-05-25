package schleifenbauer;

import com.google.inject.Guice;
import com.google.inject.Injector;

import schleifenbauer.startup.Application;
import schleifenbauer.startup.ApplicationModule;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ApplicationModule());
        injector.getInstance(Application.class).start();
    }
}
