package schleifenbauer.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import schleifenbauer.persistence.MeasurementRepository;
import schleifenbauer.rest.dto.MeasurementsResponseDto;
import schleifenbauer.service.MeasurementService;

@ExtendWith(MockitoExtension.class)
class MeasurementControllerIntegrationTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Test
    void testThat_getLatestMeasurements_responds200AndIsValid() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("channel")).thenReturn("weather/temperature");
        when(resultSet.getDouble("value")).thenReturn(18.5);
        when(resultSet.getTimestamp("timestamp")).thenReturn(Timestamp.valueOf(LocalDateTime.parse("2026-05-24T12:30:00")));

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MeasurementRepository measurementRepository = new MeasurementRepository(dataSource);
        MeasurementService measurementService = new MeasurementService(measurementRepository);
        MeasurementController controller = new MeasurementController(measurementService);

        Javalin app = Javalin.create(config -> config.jsonMapper(new JavalinJackson(objectMapper)));

        try {
            app.get("/api/measurements", controller::getLatestMeasurements);
            app.start(0);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + app.port() + "/api/measurements"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            MeasurementsResponseDto responseDto = objectMapper.readValue(response.body(), MeasurementsResponseDto.class);

            assertEquals(200, response.statusCode());
            assertEquals(1, responseDto.measurements().size());
            assertEquals("weather/temperature", responseDto.measurements().getFirst().channel());
            assertEquals(18.5, responseDto.measurements().getFirst().value());
        } finally {
            app.stop();
        }
    }
}
