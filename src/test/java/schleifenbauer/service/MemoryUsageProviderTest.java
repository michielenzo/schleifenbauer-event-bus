package schleifenbauer.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import schleifenbauer.provider.MemoryUsageProvider;

class MemoryUsageProviderTest {
    @Test
    void returnsFreeMemoryInBytes() {
        MemoryUsageProvider provider = new MemoryUsageProvider();

        long freeMemoryBytes = provider.freeMemoryBytes();

        assertTrue(freeMemoryBytes >= 0);
    }
}
