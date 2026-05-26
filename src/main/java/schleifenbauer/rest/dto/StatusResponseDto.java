package schleifenbauer.rest.dto;

public record StatusResponseDto(
        String weatherPollingJob,
        String memoryPollingJob
) {}
