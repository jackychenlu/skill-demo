package com.example.demo.service;

import com.example.demo.dto.ServerStatusResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ServerStatusService using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class ServerStatusServiceTest {
    
    @InjectMocks
    ServerStatusService serverStatusService;
    
    @Test
    void getStatus_returnsValidServerStatusResponse() {
        // Arrange
        ReflectionTestUtils.setField(serverStatusService, "appVersion", "1.0.0");
        
        // Act
        ServerStatusResponse response = serverStatusService.getStatus();
        
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("UP");
        assertThat(response.version()).isEqualTo("1.0.0");
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.uptime()).isGreaterThanOrEqualTo(0);
        assertThat(response.availableProcessors()).isGreaterThan(0);
        assertThat(response.totalMemory()).isGreaterThan(0);
        assertThat(response.freeMemory()).isGreaterThanOrEqualTo(0);
    }
    
    @Test
    void getStatus_usedMemoryIsCalculatedCorrectly() {
        // Arrange
        ReflectionTestUtils.setField(serverStatusService, "appVersion", "1.0.0");
        
        // Act
        ServerStatusResponse response = serverStatusService.getStatus();
        long expectedUsedMemory = response.totalMemory() - response.freeMemory();
        
        // Assert
        assertThat(response.usedMemory()).isEqualTo(expectedUsedMemory);
    }
    
    @Test
    void getUptime_returnsNonNegativeValue() {
        // Act
        long uptime = serverStatusService.getUptime();
        
        // Assert
        assertThat(uptime).isGreaterThanOrEqualTo(0);
    }
    
    @Test
    void getUptime_increasesOverTime() throws InterruptedException {
        // Act
        long uptime1 = serverStatusService.getUptime();
        Thread.sleep(10);
        long uptime2 = serverStatusService.getUptime();
        
        // Assert
        assertThat(uptime2).isGreaterThan(uptime1);
    }
}
