# Abeeway Smart Badge Parameter Builder - Usage Examples

This document demonstrates how to use the fluent parameter builder API to configure Abeeway Smart Badge devices without needing deep knowledge of the protocol.

## Basic Usage

### Simple Parameter Configuration

```java
import com.abeeway.smartbadge.AbeewaySmartBadgeCodec;
import com.abeeway.smartbadge.parameters.AbeewayParams;
import com.abeeway.smartbadge.models.EncodedDownlink;

// Create codec instance
AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();

// Build parameters using fluent API
AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
    .setGpsTimeout(120)                    // 2 minutes
    .setUplinkPeriod(3600)                // 1 hour
    .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
    .setBatteryLowThreshold(15)           // 15%
    .build();

// Encode parameters for transmission
EncodedDownlink downlink = codec.encodeParameters(params, 2);
byte[] bytesToSend = downlink.getBytes();
```

### Configuration Commands

```java
import com.abeeway.smartbadge.encoders.ParameterEncoder;

// Set device operating mode
ParameterEncoder.ConfigurationCommand setMode = 
    ParameterEncoder.setMode(AbeewayParams.OperatingMode.MOTION_TRACKING);
EncodedDownlink modeCommand = codec.encodeConfigurationCommand(setMode, 2);

// Request current configuration
ParameterEncoder.ConfigurationCommand requestConfig = 
    ParameterEncoder.requestConfiguration();
EncodedDownlink configRequest = codec.encodeConfigurationCommand(requestConfig, 2);

// Request position on demand
ParameterEncoder.ConfigurationCommand requestPosition = 
    ParameterEncoder.requestPosition();
EncodedDownlink positionRequest = codec.encodeConfigurationCommand(requestPosition, 2);
```

## Complete Device Configuration Examples

### GPS and Position Tracking Setup

```java
AbeewayParams gpsConfig = AbeewaySmartBadgeCodec.newParameters()
    // GPS acquisition settings
    .setGpsTimeout(180)                   // 3 minutes max for GPS fix
    .setGpsConvergenceTimeout(120)        // 2 minutes convergence timeout
    .setGpsFixMinSatellites(4)           // Require at least 4 satellites
    .setGpsFixMinSnr(25)                 // Minimum signal quality
    
    // Position reporting intervals
    .setUplinkPeriod(1800)               // Report every 30 minutes
    .setLorawanPeriod(3600)              // LoRaWAN transmission every hour
    .setPositionOnDemandTimeout(60)      // 1 minute timeout for on-demand
    
    .build();
```

### Motion Detection Configuration

```java
AbeewayParams motionConfig = AbeewaySmartBadgeCodec.newParameters()
    // Motion sensitivity and thresholds
    .setMotionSensitivity(AbeewayParams.MotionSensitivity.MEDIUM)
    .setMotionDebounceTime(10)           // 10 seconds debounce
    .setShockDetectionThreshold(1000)    // 1000mg shock threshold
    
    // Activity tracking
    .setActivityTrackingEnabled(true)
    .setStepCounterEnabled(true)
    
    .build();
```

### WiFi and BLE Scanning Setup

```java
AbeewayParams scanConfig = AbeewaySmartBadgeCodec.newParameters()
    // WiFi scanning
    .setWifiScanTimeout(20)              // 20 seconds WiFi scan
    .setWifiMaxAccessPoints(8)           // Scan up to 8 APs
    
    // BLE scanning  
    .setBleScanTimeout(15)               // 15 seconds BLE scan
    .setBleMaxBeacons(10)                // Scan up to 10 beacons
    
    .build();
```

### Power Management Configuration

```java
AbeewayParams powerConfig = AbeewaySmartBadgeCodec.newParameters()
    // Battery thresholds
    .setBatteryLowThreshold(20)          // Low battery at 20%
    .setBatteryCriticalThreshold(5)      // Critical at 5%
    .setPowerSaveMode(true)              // Enable power saving
    
    // Keep alive settings
    .setKeepAliveInterval(7200)          // 2 hours keep alive
    .setRetransmissionAttempts(3)        // 3 retry attempts
    
    .build();
```

### User Interface Configuration

```java
AbeewayParams uiConfig = AbeewaySmartBadgeCodec.newParameters()
    // Button configuration
    .setButtonPressEnabled(true)
    .setSosButtonEnabled(true)
    .setButtonLongPressThreshold(3000)   // 3 seconds for long press
    
    // Visual and audio feedback
    .setLedIndicationEnabled(true)
    .setBuzzerEnabled(false)             // Disable buzzer for quiet operation
    
    .build();
```

### Geofencing and Proximity Setup

```java
AbeewayParams locationConfig = AbeewaySmartBadgeCodec.newParameters()
    // Geofencing
    .setGeofencingEnabled(true)
    .setGeofenceRadius(100)              // 100 meter radius
    
    // Proximity detection
    .setProximityDetectionEnabled(true)
    .setProximityThreshold(-70)          // -70 dBm threshold
    
    .build();
```

### Temperature Monitoring

```java
AbeewayParams tempConfig = AbeewaySmartBadgeCodec.newParameters()
    .setTemperatureMonitoringEnabled(true)
    .setTemperatureAlertThreshold(45.0)  // Alert at 45°C
    .build();
```

## Complete Device Setup Example

```java
public class DeviceConfigurationExample {
    
    public static void configureAbeewayDevice() throws Exception {
        AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();
        
        // 1. Set operating mode to motion tracking
        ParameterEncoder.ConfigurationCommand setMode = 
            ParameterEncoder.setMode(AbeewayParams.OperatingMode.MOTION_TRACKING);
        EncodedDownlink modeCommand = codec.encodeConfigurationCommand(setMode, 2);
        sendToDevice(modeCommand.getBytes());
        
        // 2. Configure comprehensive parameters
        AbeewayParams fullConfig = AbeewaySmartBadgeCodec.newParameters()
            // GPS and positioning
            .setGpsTimeout(150)
            .setGpsConvergenceTimeout(90)
            .setUplinkPeriod(3600)           // 1 hour reporting
            .setLorawanPeriod(7200)          // 2 hour LoRaWAN
            
            // Motion and activity
            .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
            .setMotionDebounceTime(5)
            .setActivityTrackingEnabled(true)
            .setStepCounterEnabled(true)
            
            // Scanning configuration
            .setWifiScanTimeout(20)
            .setWifiMaxAccessPoints(5)
            .setBleScanTimeout(15)
            .setBleMaxBeacons(8)
            
            // Power management
            .setBatteryLowThreshold(15)
            .setBatteryCriticalThreshold(5)
            .setPowerSaveMode(true)
            
            // User interface
            .setButtonPressEnabled(true)
            .setSosButtonEnabled(true)
            .setLedIndicationEnabled(true)
            .setBuzzerEnabled(true)
            
            // Environmental monitoring
            .setTemperatureMonitoringEnabled(true)
            .setTemperatureAlertThreshold(50.0)
            
            .build();
        
        // 3. Send configuration to device
        EncodedDownlink configCommand = codec.encodeParameters(fullConfig, 2);
        sendToDevice(configCommand.getBytes());
        
        System.out.println("Device configured with " + 
                          fullConfig.getParameterNames().size() + " parameters");
    }
    
    private static void sendToDevice(byte[] data) {
        // Your LoRaWAN transmission logic here
        System.out.println("Sending " + data.length + " bytes to device");
    }
}
```

## Parameter Discovery

```java
// Get all available parameters with descriptions
Map<String, String> availableParams = codec.getAvailableParameters();
for (Map.Entry<String, String> param : availableParams.entrySet()) {
    System.out.println(param.getKey() + ": " + param.getValue());
}

// Get constraints for a specific parameter
ParameterConstraints constraints = AbeewaySmartBadgeCodec.newParameters()
    .getParameterConstraints("gpsTimeout");
System.out.println("GPS Timeout: " + constraints.getDescription());
```

## Error Handling and Validation

```java
try {
    AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
        .setGpsTimeout(500)              // Invalid: max is 300
        .build();
} catch (IllegalArgumentException e) {
    System.out.println("Parameter validation failed: " + e.getMessage());
    // Output: Parameter gpsTimeout value 500 is out of range [10, 300]
}
```

## Advanced Usage

### Custom Parameters (Advanced Users)

```java
// For advanced users who know the protocol
AbeewayParams customParams = AbeewaySmartBadgeCodec.newParameters()
    .setCustomParameter(0xFF, 42)       // Custom parameter ID 0xFF = value 42
    .setGpsTimeout(120)                 // Mix with standard parameters
    .build();
```

### Parameter Inspection

```java
AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
    .setGpsTimeout(120)
    .setUplinkPeriod(3600)
    .build();

// Inspect what was set
Set<String> paramNames = params.getParameterNames();
Object gpsTimeout = params.getParameter("gpsTimeout");
Map<String, Object> allParams = params.getParameters();
```

## Benefits of the Parameter Builder

1. **Type Safety**: Compile-time validation of parameter types
2. **Range Validation**: Automatic validation of parameter ranges
3. **Self-Documenting**: Parameter names are human-readable
4. **IDE Support**: Autocomplete and documentation in your IDE
5. **Discoverability**: Easy to find available parameters
6. **Maintainability**: No need to memorize parameter IDs or ranges

This parameter builder transforms the low-level codec into a high-level, developer-friendly SDK that makes it easy to configure Abeeway Smart Badge devices without deep protocol knowledge.