package schleifenbauer.provider;

public final class MemoryUsageProvider {
    public long freeMemoryBytes() {
        return Runtime.getRuntime().freeMemory();
    }
}
