package com.example.demo.controller;

import com.example.demo.dto.ServerStatusResponse;
import com.example.demo.service.ServerStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for server status endpoints.
 * Provides endpoints to retrieve server health and metrics.
 * Requires valid Bearer token authentication.
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
     * Requires Bearer token authentication.
     * 
     * @return 200 OK with ServerStatusResponse
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServerStatusResponse> getServerStatus() {
        ServerStatusResponse status = serverStatusService.getStatus();
        return ResponseEntity.ok(status);
    }
    
    /**
     * Get the server uptime in milliseconds.
     * Requires Bearer token authentication.
     * 
     * @return 200 OK with uptime value
     */
    @GetMapping("/uptime")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getUptime() {
        long uptime = serverStatusService.getUptime();
        return ResponseEntity.ok(String.format("{\"uptime\": %d}", uptime));
    }
}
