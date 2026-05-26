package schleifenbauer.domain;

import java.util.List;

public final class MeasurementChannels {
    public static final String TEMPERATURE = "temperature";
    public static final String HUMIDITY = "humidity";
    public static final String MEMORY_FREE_BYTES = "memory_free_bytes";
    public static final List<String> ALL = List.of(TEMPERATURE, HUMIDITY, MEMORY_FREE_BYTES);

    private MeasurementChannels() {
    }
}
