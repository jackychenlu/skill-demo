package com.example.demo.controller;

import com.example.demo.dto.ServerStatusResponse;
import com.example.demo.service.ServerStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for server status endpoints.
 * Provides endpoints to retrieve server health and metrics.
 */
@RestController
@RequestMapping("/api/server-status")
public class ServerStatusController {
    
    private final ServerStatusService serverStatusService;
    
    public ServerStatusController(ServerStatusService serverStatusService) {
        this.serverStatusService = serverStatusService;
    }
    
    /**
     * Get the current server status.
     * 
     * @return 200 OK with ServerStatusResponse
     */
    @GetMapping
    public ResponseEntity<ServerStatusResponse> getServerStatus() {
        ServerStatusResponse status = serverStatusService.getStatus();
        return ResponseEntity.ok(status);
    }
    
    /**
     * Get the server uptime in milliseconds.
     * 
     * @return 200 OK with uptime value
     */
    @GetMapping("/uptime")
    public ResponseEntity<String> getUptime() {
        long uptime = serverStatusService.getUptime();
        return ResponseEntity.ok(String.format("{\"uptime\": %d}", uptime));
    }
}
