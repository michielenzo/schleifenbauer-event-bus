package schleifenbauer.bootstrap;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import schleifenbauer.eventbus.EventBus;
import schleifenbauer.eventbus.InMemoryEventBus;
import schleifenbauer.scheduling.StartupTask;
import schleifenbauer.weather.OpenMeteoWeatherClient;
import schleifenbauer.weather.WeatherClient;
import schleifenbauer.weather.WeatherPollingTask;

public final class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).to(InMemoryEventBus.class).in(Singleton.class);
        bind(WeatherClient.class).to(OpenMeteoWeatherClient.class).in(Singleton.class);
        bind(StartupTask.class).to(WeatherPollingTask.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    HttpClient provideHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Provides
    @Singleton
    ScheduledExecutorService provideScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
