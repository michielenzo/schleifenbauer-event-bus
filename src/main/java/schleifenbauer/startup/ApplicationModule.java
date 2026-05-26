package schleifenbauer.startup;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import schleifenbauer.client.OpenMeteoWeatherClient;
import schleifenbauer.client.WeatherClient;
import schleifenbauer.dal.DataSourceProvider;
import schleifenbauer.dal.DatabaseConfig;
import schleifenbauer.infrastructure.eventbus.EventBus;
import schleifenbauer.infrastructure.eventbus.InMemoryEventBus;

public final class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).to(InMemoryEventBus.class).in(Singleton.class);
        bind(WeatherClient.class).to(OpenMeteoWeatherClient.class).in(Singleton.class);
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
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Provides
    @Singleton
    ScheduledExecutorService provideScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Provides
    @Singleton
    DatabaseConfig provideDatabaseConfig() {
        return new DatabaseConfig(
                requireEnv("DB_HOST"),
                Integer.parseInt(requireEnv("DB_PORT")),
                requireEnv("DB_NAME"),
                requireEnv("DB_USER"),
                requireEnv("DB_PASSWORD"));
    }

    @Provides
    @Singleton
    DataSource provideDataSource(DataSourceProvider dataSourceProvider) {
        return dataSourceProvider.dataSource();
    }

    private static String requireEnv(String key) {
        String value = System.getenv(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }

        return value;
    }
}
