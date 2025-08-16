# Abeeway Smart Badge Configuration Workflow

This document explains the complete workflow for configuring Abeeway Smart Badge devices through ChirpStack and handling responses.

## 🔄 Complete Configuration Workflow

### Step 1: Request Current Configuration

```java
AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();

// 1. Request current configuration
ParameterEncoder.ConfigurationCommand requestConfig = 
    ParameterEncoder.requestConfiguration();

EncodedDownlink configRequest = codec.encodeConfigurationCommand(requestConfig, 2);

// 2. Send via ChirpStack
sendToChirpStack(configRequest.getBytes(), 2, true); // confirmed=true for ACK
```

### Step 2: Receive and Process Configuration Response

```java
// When you receive uplink from ChirpStack webhook/API
public void handleUplinkFromChirpStack(byte[] payload, int fPort, String timestamp) {
    try {
        DecodedUplink response = codec.decodeUplink(payload, fPort, timestamp);
        UplinkData data = (UplinkData) response.getData();
        
        if (data.getMessageType() == MessageType.CONFIGURATION) {
            Map<String, Object> currentConfig = data.getDeviceConfiguration();
            
            System.out.println("=== Current Badge Configuration ===");
            for (Map.Entry<String, Object> param : currentConfig.entrySet()) {
                System.out.println(param.getKey() + ": " + param.getValue());
            }
            
            // Now you can decide what to change
            updateConfigurationIfNeeded(currentConfig);
        }
    } catch (Exception e) {
        System.err.println("Failed to decode configuration: " + e.getMessage());
    }
}
```

### Step 3: Update Configuration (If Needed)

```java
private void updateConfigurationIfNeeded(Map<String, Object> currentConfig) {
    // Check current values and decide what to change
    Object currentGpsTimeout = currentConfig.get("gpsTimeout");
    Object currentUplinkPeriod = currentConfig.get("uplinkPeriod");
    
    // Build new configuration with desired changes
    AbeewayParams newParams = AbeewaySmartBadgeCodec.newParameters()
        .setGpsTimeout(180)                    // Change to 3 minutes
        .setUplinkPeriod(7200)                // Change to 2 hours
        .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
        .setBatteryLowThreshold(15)
        .build();
    
    try {
        EncodedDownlink configUpdate = codec.encodeParameters(newParams, 2);
        sendToChirpStack(configUpdate.getBytes(), 2, true); // Send with confirmation
        
        System.out.println("Configuration update sent to badge");
    } catch (Exception e) {
        System.err.println("Failed to encode new configuration: " + e.getMessage());
    }
}
```

### Step 4: Handle Configuration Acknowledgment

```java
// Monitor for acknowledgment/confirmation
public void handleConfigurationAck(byte[] payload, int fPort, String timestamp) {
    try {
        DecodedUplink ack = codec.decodeUplink(payload, fPort, timestamp);
        UplinkData data = (UplinkData) ack.getData();
        
        // Configuration changes often result in a new configuration message
        // or an acknowledgment event
        if (data.getMessageType() == MessageType.CONFIGURATION) {
            System.out.println("✅ Configuration update confirmed!");
            
            // Verify the new settings
            Map<String, Object> updatedConfig = data.getDeviceConfiguration();
            Object newGpsTimeout = updatedConfig.get("gpsTimeout");
            System.out.println("New GPS timeout: " + newGpsTimeout);
            
        } else if (data.getMessageType() == MessageType.EVENT) {
            System.out.println("📡 Configuration event received");
        }
        
    } catch (Exception e) {
        System.err.println("Failed to process acknowledgment: " + e.getMessage());
    }
}
```

## 🌐 ChirpStack Integration

### Sending Downlinks via ChirpStack API

```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ChirpStackIntegration {
    
    private final String chirpStackUrl;
    private final String apiKey;
    private final HttpClient httpClient;
    
    public ChirpStackIntegration(String chirpStackUrl, String apiKey) {
        this.chirpStackUrl = chirpStackUrl;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }
    
    /**
     * Send downlink to device via ChirpStack
     */
    public void sendDownlink(String deviceEui, byte[] payload, int fPort, boolean confirmed) {
        try {
            // Convert payload to base64
            String payloadB64 = java.util.Base64.getEncoder().encodeToString(payload);
            
            // Build ChirpStack downlink JSON
            String json = String.format("""
                {
                  "queueItem": {
                    "confirmed": %s,
                    "fPort": %d,
                    "data": "%s"
                  }
                }
                """, confirmed, fPort, payloadB64);
            
            // Send HTTP request to ChirpStack
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(chirpStackUrl + "/api/devices/" + deviceEui + "/queue"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                System.out.println("✅ Downlink queued successfully");
            } else {
                System.err.println("❌ Failed to queue downlink: " + response.body());
            }
            
        } catch (Exception e) {
            System.err.println("❌ ChirpStack API error: " + e.getMessage());
        }
    }
}
```

### Receiving Uplinks via ChirpStack Webhook

```java
@RestController
public class ChirpStackWebhookController {
    
    private final AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();
    
    /**
     * Handle uplink webhook from ChirpStack
     */
    @PostMapping("/webhook/uplink")
    public ResponseEntity<String> handleUplink(@RequestBody Map<String, Object> payload) {
        try {
            // Extract data from ChirpStack webhook payload
            Map<String, Object> deviceInfo = (Map<String, Object>) payload.get("deviceInfo");
            String deviceEui = (String) deviceInfo.get("devEui");
            
            Map<String, Object> rxInfo = (Map<String, Object>) payload.get("rxInfo");
            String timestamp = (String) rxInfo.get("time");
            
            String dataB64 = (String) payload.get("data");
            int fPort = ((Number) payload.get("fPort")).intValue();
            
            // Decode base64 payload
            byte[] payloadBytes = java.util.Base64.getDecoder().decode(dataB64);
            
            // Decode using Abeeway codec
            DecodedUplink result = codec.decodeUplink(payloadBytes, fPort, timestamp);
            UplinkData data = (UplinkData) result.getData();
            
            System.out.println("📡 Received " + data.getMessageType() + " from " + deviceEui);
            
            // Handle different message types
            switch (data.getMessageType()) {
                case CONFIGURATION:
                    handleConfigurationMessage(deviceEui, data);
                    break;
                case POSITION_MESSAGE:
                    handlePositionMessage(deviceEui, data);
                    break;
                case EVENT:
                    handleEventMessage(deviceEui, data);
                    break;
                default:
                    System.out.println("📦 Other message type: " + data.getMessageType());
            }
            
            return ResponseEntity.ok("Processed");
            
        } catch (Exception e) {
            System.err.println("❌ Webhook processing error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    private void handleConfigurationMessage(String deviceEui, UplinkData data) {
        Map<String, Object> config = data.getDeviceConfiguration();
        System.out.println("⚙️ Configuration from " + deviceEui + ":");
        config.forEach((key, value) -> 
            System.out.println("  " + key + ": " + value));
    }
    
    private void handlePositionMessage(String deviceEui, UplinkData data) {
        if (data.getGpsLatitude() != null && data.getGpsLongitude() != null) {
            System.out.println("📍 Position from " + deviceEui + ": " + 
                data.getGpsLatitude() + ", " + data.getGpsLongitude());
        }
    }
    
    private void handleEventMessage(String deviceEui, UplinkData data) {
        System.out.println("🔔 Event from " + deviceEui + ": " + data.getEventType());
    }
}
```

## 📋 Available Configuration Commands

The library provides these configuration commands:

```java
// 1. Request current configuration
ParameterEncoder.requestConfiguration()

// 2. Set operating mode
ParameterEncoder.setMode(AbeewayParams.OperatingMode.MOTION_TRACKING)

// 3. Request position on demand
ParameterEncoder.requestPosition()

// 4. Reset configuration to defaults
ParameterEncoder.resetConfiguration()
```

## ⚡ Quick Configuration Example

```java
public class QuickConfigExample {
    
    public static void main(String[] args) throws Exception {
        AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();
        ChirpStackIntegration chirpStack = new ChirpStackIntegration(
            "https://your-chirpstack.com", "your-api-key");
        
        String deviceEui = "1234567890abcdef";
        
        // 1. Request current config
        System.out.println("🔍 Requesting current configuration...");
        EncodedDownlink configRequest = codec.encodeConfigurationCommand(
            ParameterEncoder.requestConfiguration(), 2);
        chirpStack.sendDownlink(deviceEui, configRequest.getBytes(), 2, true);
        
        // Wait for response, then send new config...
        Thread.sleep(30000); // Wait 30 seconds for response
        
        // 2. Send new configuration
        System.out.println("⚙️ Sending new configuration...");
        AbeewayParams newConfig = AbeewaySmartBadgeCodec.newParameters()
            .setGpsTimeout(120)
            .setUplinkPeriod(3600)
            .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
            .build();
        
        EncodedDownlink configUpdate = codec.encodeParameters(newConfig, 2);
        chirpStack.sendDownlink(deviceEui, configUpdate.getBytes(), 2, true);
        
        System.out.println("✅ Configuration commands sent!");
    }
}
```

## 🔔 Expected Response Timeline

1. **Configuration Request**: Badge responds within 30-60 seconds
2. **Parameter Update**: Badge may respond immediately or on next scheduled uplink
3. **Mode Change**: Usually takes effect immediately with confirmation
4. **Position Request**: Badge responds within the position timeout period

## 🛠️ Troubleshooting

### No Response from Badge:
- Check ChirpStack device connectivity
- Verify downlink was queued successfully
- Ensure badge is not in deep sleep mode
- Check fPort compatibility (usually port 2 for configuration)

### Configuration Not Applied:
- Some parameters require device restart
- Check parameter validation (ranges, types)
- Verify badge firmware version compatibility
- Some parameters may be read-only

This workflow gives you complete control over badge configuration with proper acknowledgments and error handling!