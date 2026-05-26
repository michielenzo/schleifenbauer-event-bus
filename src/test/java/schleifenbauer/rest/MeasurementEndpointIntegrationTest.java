package schleifenbauer.rest;

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

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import schleifenbauer.mapper.MeasurementMapper;
import schleifenbauer.persistence.MeasurementRepository;
import schleifenbauer.rest.dto.ErrorResponseDto;
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

    private ObjectMapper objectMapper;
    private Javalin app;
    private HttpRequest request;

    private static final int PORT = 0;

    private final static double TEMPERATURE = 18.5; 
    private final static String CHANNEL = "weather/temperature";
    private static final Long ID = 1l;
    private static final String ERROR_DTO_MSG = "An internal server error occurred.";
    private static final LocalDateTime MEASUREMENT_TIMESTAMP = LocalDateTime.parse("2026-05-24T12:30:00");

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MeasurementRepository measurementRepository = new MeasurementRepository(dataSource);
        MeasurementService measurementService = new MeasurementService(measurementRepository, new MeasurementMapper());
        MeasurementController controller = new MeasurementController(measurementService);

        app = Javalin.create(config -> config.jsonMapper(new JavalinJackson(objectMapper)));
        app.get("/api/measurements", controller::getLatestMeasurements);
        app.start(PORT);

        request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + app.port() + "/api/measurements"))
            .GET()
            .build();
    }

    @Test
    void getLatestMeasurementsRespondsWith200AndMeasurements() throws Exception {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(ID);
        when(resultSet.getString("channel")).thenReturn(CHANNEL);
        when(resultSet.getDouble("value")).thenReturn(TEMPERATURE);
        when(resultSet.getTimestamp("timestamp")).thenReturn(Timestamp.valueOf(MEASUREMENT_TIMESTAMP));

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        MeasurementsResponseDto responseDto = objectMapper.readValue(response.body(), MeasurementsResponseDto.class);

        assertEquals(200, response.statusCode());
        assertEquals(1, responseDto.measurements().size());
        assertEquals(CHANNEL, responseDto.measurements().getFirst().channel());
        assertEquals(TEMPERATURE, responseDto.measurements().getFirst().value());
        assertEquals(MEASUREMENT_TIMESTAMP, responseDto.measurements().getFirst().timestamp());

        verify(preparedStatement).executeQuery();
    }

    @Test
    void getLatestMeasurementsRespondsWith500OnException() throws Exception {
        when(preparedStatement.executeQuery()).thenThrow(new java.sql.SQLException("boom"));

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ErrorResponseDto responseDto = objectMapper.readValue(response.body(), ErrorResponseDto.class);

        assertEquals(500, response.statusCode());
        assertEquals(ERROR_DTO_MSG, responseDto.message());

        verify(preparedStatement).executeQuery();
    }

    @AfterEach
    @SuppressWarnings("unused")
    void tearDown() {
        if (app != null) {
            app.stop();
        }
    }
}
