package com.example.demo.controller;

import com.example.demo.dto.ServerStatusResponse;
import com.example.demo.service.ServerStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name = "Server Status", description = "Endpoints for monitoring server health and status")
public class ServerStatusController {

    private static final Logger log = LoggerFactory.getLogger(ServerStatusController.class);
    
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
    @Operation(
        summary = "Get server status",
        description = "Returns the current server status including uptime and memory information",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ServerStatusResponse> getServerStatus() {
        log.debug("Server status requested");
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
    @Operation(
        summary = "Get server uptime",
        description = "Returns the server uptime in milliseconds",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> getUptime() {
        log.debug("Server uptime requested");
        long uptime = serverStatusService.getUptime();
        return ResponseEntity.ok(String.format("{\"uptime\": %d}", uptime));
    }
}
