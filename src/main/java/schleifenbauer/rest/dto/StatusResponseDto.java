package schleifenbauer.rest.dto;

public record StatusResponseDto(
        String weatherCollectorJob,
        String memoryCollectorJob
) {}
