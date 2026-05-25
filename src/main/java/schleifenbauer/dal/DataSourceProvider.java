package schleifenbauer.dal;

import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Singleton
public final class DataSourceProvider {
    private final HikariDataSource dataSource;

    @Inject
    public DataSourceProvider(DatabaseConfig databaseConfig) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseConfig.jdbcUrl());
        hikariConfig.setUsername(databaseConfig.user());
        hikariConfig.setPassword(databaseConfig.password());
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(1);

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public DataSource dataSource() {
        return dataSource;
    }
}
