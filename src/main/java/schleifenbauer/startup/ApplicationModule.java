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
import schleifenbauer.infrastructure.eventbus.IEventBus;
import schleifenbauer.infrastructure.eventbus.InMemoryEventBus;
import schleifenbauer.persistence.dal.DataSourceProvider;
import schleifenbauer.persistence.dal.DatabaseConfig;

public final class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IEventBus.class).to(InMemoryEventBus.class).in(Singleton.class);
        bind(WeatherClient.class).to(OpenMeteoWeatherClient.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    ScheduledExecutorService provideScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
