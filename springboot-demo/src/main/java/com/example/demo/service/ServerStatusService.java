package com.example.demo.service;

import com.example.demo.dto.ServerStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving server status and system metrics.
 */
@Service
public class ServerStatusService {

    private static final Logger log = LoggerFactory.getLogger(ServerStatusService.class);
    
    @Value("${app.version:0.1.0}")
    private String appVersion;
    
    private final long startTime;
    
    public ServerStatusService() {
        this.startTime = System.currentTimeMillis();
        log.info("ServerStatusService initialized at timestamp: {}", startTime);
    }
    
    /**
     * Get the current server status with system metrics.
     * 
     * @return ServerStatusResponse containing status and metrics
     */
    public ServerStatusResponse getStatus() {
        log.debug("Retrieving server status");
        Runtime runtime = Runtime.getRuntime();
        
        ServerStatusResponse response = ServerStatusResponse.of(
            appVersion,
            startTime,
            runtime.availableProcessors(),
            runtime.totalMemory(),
            runtime.freeMemory()
        );
        
        log.debug("Server status retrieved: version={}, uptime={}ms", appVersion, response.uptime());
        return response;
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
