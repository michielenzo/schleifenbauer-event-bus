package schleifenbauer.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.google.inject.Inject;

import schleifenbauer.persistence.entity.MeasurementEntity;

public final class MeasurementRepository {

    private static final String INSERT_QUERY = """
            INSERT INTO measurements (channel, value, timestamp)
            VALUES (?, ?, ?)
            """;
            
    private static final String FIND_LATEST_QUERY = """
            SELECT id, channel, value, timestamp
            FROM measurements
            ORDER BY timestamp DESC
            LIMIT ?
            """;

    private static final String FIND_LATEST_BY_CHANNEL_QUERY = """
            SELECT id, channel, value, timestamp
            FROM measurements
            WHERE channel = ?
            ORDER BY timestamp DESC
            LIMIT ?
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

    public List<MeasurementEntity> findLatest(int limit) throws SQLException {
        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_LATEST_QUERY)
        ) {
            statement.setInt(1, limit);

            return readMeasurements(statement);
        }
    }

    public List<MeasurementEntity> findLatestByChannel(String channel, int limit) throws SQLException {
        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_LATEST_BY_CHANNEL_QUERY)
        ) {
            statement.setString(1, channel);
            statement.setInt(2, limit);

            return readMeasurements(statement);
        }
    }

    private List<MeasurementEntity> readMeasurements(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            List<MeasurementEntity> measurements = new ArrayList<>();

            while (resultSet.next()) {
                measurements.add(new MeasurementEntity(
                        resultSet.getLong("id"),
                        resultSet.getString("channel"),
                        resultSet.getDouble("value"),
                        resultSet.getTimestamp("timestamp").toLocalDateTime()));
            }

            return measurements;
        }
    }
}
