package com.abeeway.smartbadge;

import com.abeeway.smartbadge.exceptions.EncodingException;
import com.abeeway.smartbadge.models.EncodedDownlink;
import com.abeeway.smartbadge.parameters.AbeewayParams;
import com.abeeway.smartbadge.encoders.ParameterEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * Test suite for the parameter builder functionality.
 */
public class ParameterBuilderTest {

    private AbeewaySmartBadgeCodec codec;

    @BeforeEach
    void setUp() {
        codec = new AbeewaySmartBadgeCodec();
    }

    @Test
    @Order(1)
    void testBasicParameterBuilder() throws EncodingException {
        // Build parameters using fluent API
        AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
            .setGpsTimeout(120)
            .setUplinkPeriod(3600)
            .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
            .setBatteryLowThreshold(15)
            .build();
        
        assertNotNull(params);
        assertEquals(4, params.getParameterNames().size());
        assertEquals(120, params.getParameter("gpsTimeout"));
        assertEquals(3600, params.getParameter("uplinkPeriod"));
    }
    
    @Test
    @Order(2)
    void testParameterEncoding() throws EncodingException {
        AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
            .setGpsTimeout(90)
            .setWifiScanTimeout(15)
            .setMotionSensitivity(AbeewayParams.MotionSensitivity.MEDIUM)
            .build();
        
        EncodedDownlink downlink = codec.encodeParameters(params, 2);
        
        assertNotNull(downlink);
        assertNotNull(downlink.getBytes());
        assertTrue(downlink.getBytes().length > 0);
        assertEquals(2, downlink.getfPort());
    }
    
    @Test
    @Order(3)
    void testConfigurationCommands() throws EncodingException {
        // Test mode setting
        ParameterEncoder.ConfigurationCommand setMode = 
            ParameterEncoder.setMode(AbeewayParams.OperatingMode.MOTION_TRACKING);
        
        EncodedDownlink modeDownlink = codec.encodeConfigurationCommand(setMode, 2);
        assertNotNull(modeDownlink);
        
        // Test config request
        ParameterEncoder.ConfigurationCommand requestConfig = 
            ParameterEncoder.requestConfiguration();
        
        EncodedDownlink configDownlink = codec.encodeConfigurationCommand(requestConfig, 2);
        assertNotNull(configDownlink);
        
        // Test position request
        ParameterEncoder.ConfigurationCommand requestPosition = 
            ParameterEncoder.requestPosition();
        
        EncodedDownlink positionDownlink = codec.encodeConfigurationCommand(requestPosition, 2);
        assertNotNull(positionDownlink);
    }
    
    @Test
    @Order(4)
    void testParameterValidation() {
        // Test valid parameters
        assertDoesNotThrow(() -> {
            AbeewaySmartBadgeCodec.newParameters()
                .setGpsTimeout(120) // Valid: 10-300
                .setUplinkPeriod(3600) // Valid: 60-86400
                .build();
        });
        
        // Test invalid parameters
        assertThrows(IllegalArgumentException.class, () -> {
            AbeewaySmartBadgeCodec.newParameters()
                .setGpsTimeout(5) // Invalid: below minimum (10)
                .build();
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            AbeewaySmartBadgeCodec.newParameters()
                .setBatteryLowThreshold(60) // Invalid: above maximum (50)
                .build();
        });
    }
    
    @Test
    @Order(5)
    void testComplexParameterConfiguration() throws EncodingException {
        // Build a comprehensive parameter set
        AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
            // GPS settings
            .setGpsTimeout(180)
            .setGpsConvergenceTimeout(120)
            .setGpsFixMinSatellites(4)
            
            // Position reporting
            .setUplinkPeriod(1800) // 30 minutes
            .setLorawanPeriod(3600) // 1 hour
            
            // Motion detection
            .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
            .setMotionDebounceTime(5)
            .setShockDetectionThreshold(800)
            
            // WiFi and BLE
            .setWifiScanTimeout(20)
            .setWifiMaxAccessPoints(5)
            .setBleScanTimeout(15)
            .setBleMaxBeacons(8)
            
            // Battery and power
            .setBatteryLowThreshold(20)
            .setBatteryCriticalThreshold(10)
            .setPowerSaveMode(true)
            
            // Activity tracking
            .setActivityTrackingEnabled(true)
            .setStepCounterEnabled(true)
            
            // Button configuration
            .setButtonPressEnabled(true)
            .setSosButtonEnabled(true)
            .setButtonLongPressThreshold(2000)
            
            // LED and buzzer
            .setLedIndicationEnabled(true)
            .setBuzzerEnabled(false)
            
            .build();
        
        // Verify parameters were set
        assertTrue(params.getParameterNames().size() >= 15);
        assertEquals(180, params.getParameter("gpsTimeout"));
        assertEquals(1800, params.getParameter("uplinkPeriod"));
        assertEquals(1, params.getParameter("powerSaveMode")); // boolean -> 1
        
        // Test encoding
        EncodedDownlink downlink = codec.encodeParameters(params, 2);
        assertNotNull(downlink);
        assertTrue(downlink.getBytes().length > 10); // Should be substantial
    }
    
    @Test
    @Order(6)
    void testParameterDiscovery() {
        // Test parameter discovery
        Map<String, String> availableParams = codec.getAvailableParameters();
        
        assertNotNull(availableParams);
        assertTrue(availableParams.size() > 20); // Should have many parameters
        
        // Verify some key parameters are documented
        assertTrue(availableParams.containsKey("gpsTimeout"));
        assertTrue(availableParams.containsKey("uplinkPeriod"));
        assertTrue(availableParams.containsKey("motionSensitivity"));
        
        // Check that descriptions include ranges
        String gpsTimeoutDesc = availableParams.get("gpsTimeout");
        assertTrue(gpsTimeoutDesc.contains("10-300")); // Should show range
    }
    
    @Test
    @Order(7)
    void testOperatingModes() throws EncodingException {
        // Test all operating modes
        for (AbeewayParams.OperatingMode mode : AbeewayParams.OperatingMode.values()) {
            ParameterEncoder.ConfigurationCommand command = ParameterEncoder.setMode(mode);
            EncodedDownlink downlink = codec.encodeConfigurationCommand(command, 2);
            
            assertNotNull(downlink);
            assertNotNull(downlink.getBytes());
            assertTrue(downlink.getBytes().length >= 2); // Command + mode byte
        }
    }
    
    @Test
    @Order(8)
    void testMotionSensitivityLevels() throws EncodingException {
        // Test all motion sensitivity levels
        for (AbeewayParams.MotionSensitivity sensitivity : AbeewayParams.MotionSensitivity.values()) {
            AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
                .setMotionSensitivity(sensitivity)
                .build();
            
            assertEquals(sensitivity.getValue(), params.getParameter("motionSensitivity"));
            
            // Test encoding
            EncodedDownlink downlink = codec.encodeParameters(params, 2);
            assertNotNull(downlink);
        }
    }
}