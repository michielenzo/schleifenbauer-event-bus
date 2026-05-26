package schleifenbauer.startup;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class ApplicationModuleTest {
    @Test
    void serializesLocalDateTimeAsIsoString() throws Exception {
        ApplicationModule module = new ApplicationModule();
        ObjectMapper objectMapper = module.provideObjectMapper();

        String json = objectMapper.writeValueAsString(new TimestampDto(LocalDateTime.parse("2026-05-26T13:29:08")));

        assertEquals("{\"timestamp\":\"2026-05-26T13:29:08\"}", json);
    }

    private record TimestampDto(LocalDateTime timestamp) {
    }
}
