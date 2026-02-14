package com.example.demo.controller;

import com.example.demo.dto.ServerStatusResponse;
import com.example.demo.service.ServerStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ServerStatusController using MockMvc.
 * Tests token-based authentication and endpoint behavior.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ServerStatusControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ServerStatusService serverStatusService;

    private static final String VALID_TOKEN = "Bearer demo-secret-key-change-in-production";
    
    @Test
    void getServerStatus_returns200Ok() throws Exception {
        // Arrange
        ServerStatusResponse response = new ServerStatusResponse(
            "UP",
            "1.0.0",
            Instant.now(),
            1000L,
            8,
            1024000000L,
            512000000L,
            512000000L
        );
        when(serverStatusService.getStatus()).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(get("/api/server-status")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", VALID_TOKEN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", equalTo("UP")))
            .andExpect(jsonPath("$.version", equalTo("1.0.0")))
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.uptime", greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.availableProcessors", equalTo(8)))
            .andExpect(jsonPath("$.totalMemory", greaterThan(0)))
            .andExpect(jsonPath("$.freeMemory", greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.usedMemory", greaterThanOrEqualTo(0)));
    }
    
    @Test
    void getServerStatus_returns403WithoutToken() throws Exception {
        // Act & Assert - Without valid token, should get 403 Forbidden
        mockMvc.perform(get("/api/server-status")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
    
    @Test
    void getServerStatus_containsAllRequiredFields() throws Exception {
        // Arrange
        ServerStatusResponse response = new ServerStatusResponse(
            "UP",
            "1.0.0",
            Instant.now(),
            5000L,
            4,
            2048000000L,
            1024000000L,
            1024000000L
        );
        when(serverStatusService.getStatus()).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(get("/api/server-status")
                .header("Authorization", VALID_TOKEN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.status").exists())
            .andExpect(jsonPath("$.version").exists())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.uptime").exists())
            .andExpect(jsonPath("$.availableProcessors").exists())
            .andExpect(jsonPath("$.totalMemory").exists())
            .andExpect(jsonPath("$.freeMemory").exists())
            .andExpect(jsonPath("$.usedMemory").exists());
    }
    
    @Test
    void getUptime_returns200OkWithUptimeValue() throws Exception {
        // Arrange
        long uptime = 12345L;
        when(serverStatusService.getUptime()).thenReturn(uptime);
        
        // Act & Assert
        mockMvc.perform(get("/api/server-status/uptime")
                .header("Authorization", VALID_TOKEN))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"uptime\": 12345")));
    }
    
    @Test
    void getServerStatus_respondsWithApplicationJsonContentType() throws Exception {
        // Arrange
        ServerStatusResponse response = new ServerStatusResponse(
            "UP",
            "1.0.0",
            Instant.now(),
            1000L,
            8,
            1024000000L,
            512000000L,
            512000000L
        );
        when(serverStatusService.getStatus()).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(get("/api/server-status")
                .header("Authorization", VALID_TOKEN))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
