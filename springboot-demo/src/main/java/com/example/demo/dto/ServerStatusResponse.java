package com.example.demo.dto;

import java.time.Instant;

/**
 * DTO for server status response.
 * Represents the current state of the application server.
 */
public record ServerStatusResponse(
    String status,
    String version,
    Instant timestamp,
    Long uptime,
    int availableProcessors,
    long totalMemory,
    long freeMemory,
    long usedMemory
) {
    /**
     * Create a serverStatusResponse from system metrics.
     */
    public static ServerStatusResponse of(
        String version,
        long startTime,
        int availableProcessors,
        long totalMemory,
        long freeMemory
    ) {
        long usedMemory = totalMemory - freeMemory;
        long uptime = System.currentTimeMillis() - startTime;
        
        return new ServerStatusResponse(
            "UP",
            version,
            Instant.now(),
            uptime,
            availableProcessors,
            totalMemory,
            freeMemory,
            usedMemory
        );
    }
}
