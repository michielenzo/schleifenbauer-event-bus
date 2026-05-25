package schleifenbauer.provider;

import com.google.inject.Singleton;

@Singleton
public final class MemoryUsageProvider {
    public long freeMemoryBytes() {
        return Runtime.getRuntime().freeMemory();
    }
}
