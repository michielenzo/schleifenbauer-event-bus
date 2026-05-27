package schleifenbauer.persistence.dal;

import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Singleton
public final class DataSourceProvider {
    private final HikariDataSource dataSource;

    private static final int MAX_POOL_SIZE = 10;
    private static final int MINIMUM_IDLE = 1;

    @Inject
    public DataSourceProvider(DatabaseConfig databaseConfig) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseConfig.jdbcUrl());
        hikariConfig.setUsername(databaseConfig.user());
        hikariConfig.setPassword(databaseConfig.password());
        hikariConfig.setMaximumPoolSize(MAX_POOL_SIZE);
        hikariConfig.setMinimumIdle(MINIMUM_IDLE);

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public DataSource dataSource() {
        return dataSource;
    }
}
