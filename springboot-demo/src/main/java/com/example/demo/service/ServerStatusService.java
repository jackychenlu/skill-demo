package com.example.demo.service;

import com.example.demo.dto.ServerStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving server status and system metrics.
 */
@Service
public class ServerStatusService {
    
    @Value("${app.version:0.1.0}")
    private String appVersion;
    
    private final long startTime;
    
    public ServerStatusService() {
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * Get the current server status with system metrics.
     * 
     * @return ServerStatusResponse containing status and metrics
     */
    public ServerStatusResponse getStatus() {
        Runtime runtime = Runtime.getRuntime();
        
        return ServerStatusResponse.of(
            appVersion,
            startTime,
            runtime.availableProcessors(),
            runtime.totalMemory(),
            runtime.freeMemory()
        );
    }
    
    /**
     * Get the application uptime in milliseconds.
     * 
     * @return uptime in milliseconds
     */
    public long getUptime() {
        return System.currentTimeMillis() - startTime;
    }
}
