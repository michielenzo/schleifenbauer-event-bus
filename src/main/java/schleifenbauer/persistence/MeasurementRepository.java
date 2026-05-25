package schleifenbauer.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import schleifenbauer.persistence.entity.MeasurementEntity;

@Singleton
public final class MeasurementRepository {

    private static final String INSERT_QUERY = """
            INSERT INTO measurements (channel, value, timestamp)
            VALUES (?, ?, ?)
            """;

    private final DataSource dataSource;

    @Inject
    public MeasurementRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(MeasurementEntity measurementEntity) throws SQLException {
        try (
            Connection connection = dataSource.getConnection(); 
            PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)
        ) {
            statement.setString(1, measurementEntity.channel());
            statement.setDouble(2, measurementEntity.value());
            statement.setTimestamp(3, Timestamp.valueOf(measurementEntity.timestamp()));
            statement.executeUpdate();
        }
    }
}
