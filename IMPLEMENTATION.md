# Abeeway Smart Badge Java Library Implementation Guide

This document provides comprehensive instructions for implementing a full-featured Java library that replicates ALL functionality of the Abeeway Smart Badge JavaScript driver.

## Project Overview

**Goal**: Create a standalone Java library that provides complete feature parity with the JavaScript driver, exposing all decoding/encoding capabilities for:
- GPS/WiFi/BLE position decoding
- Device configuration management (200+ parameters)
- Event handling (button presses, motion, SOS, etc.)
- Battery and health status monitoring
- Downlink command encoding
- All 19 message types supported by the original driver

## Prerequisites

- IntelliJ IDEA
- Java 11+ (preferably Java 17+)
- Maven or Gradle build system
- Jackson library for JSON handling
- JUnit 5 for testing

## Project Structure

```
abeeway-smartbadge-java/
├── src/main/java/com/abeeway/smartbadge/
│   ├── AbeewaySmartBadgeCodec.java          # Main API class
│   ├── models/                               # Data classes
│   │   ├── DecodedUplink.java
│   │   ├── EncodedDownlink.java
│   │   ├── PositionData.java
│   │   ├── ConfigurationData.java
│   │   └── ...
│   ├── enums/                               # All enum constants
│   │   ├── MessageType.java
│   │   ├── RawPositionType.java
│   │   ├── BatteryStatus.java
│   │   └── ...
│   ├── decoders/                            # Message-specific decoders
│   │   ├── PositionDecoder.java
│   │   ├── ConfigurationDecoder.java
│   │   ├── EventDecoder.java
│   │   └── ...
│   ├── encoders/                            # Downlink encoders
│   │   ├── ConfigurationEncoder.java
│   │   ├── CommandEncoder.java
│   │   └── ...
│   ├── utils/                               # Utility classes
│   │   ├── BitUtils.java
│   │   ├── ByteUtils.java
│   │   └── ValidationUtils.java
│   └── exceptions/                          # Custom exceptions
│       ├── DecodingException.java
│       └── EncodingException.java
├── src/test/java/                           # Test classes
├── src/test/resources/
│   ├── examples.json                        # Copy from JS driver
│   └── test-vectors/
└── pom.xml                                  # Maven configuration
```

## Implementation Steps

### Phase 1: Project Setup and Core Infrastructure

#### 1.1 Create Maven Project
```xml
<!-- pom.xml -->
<project>
    <groupId>com.abeeway</groupId>
    <artifactId>smartbadge-codec</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <jackson.version>2.15.2</jackson.version>
        <junit.version>5.10.0</junit.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

#### 1.2 Copy Reference Data
Copy these files from the JavaScript driver to `src/test/resources/`:
- `examples.json` (390+ test examples)
- `src/resources/parameters.json` (parameter definitions)
- `src/resources/profiles.json` (profile definitions)

### Phase 2: Enums and Constants

#### 2.1 Port All Enums
Create Java enums for each JavaScript enum. **Critical**: These must match exactly.

```java
// enums/MessageType.java
public enum MessageType {
    POSITION_MESSAGE,
    EXTENDED_POSITION_MESSAGE,
    HEARTBEAT,
    ENERGY_STATUS,
    HEALTH_STATUS,
    SHUTDOWN,
    FRAME_PENDING,
    DEBUG,
    ACTIVITY_STATUS,
    CONFIGURATION,
    SHOCK_DETECTION,
    BLE_MAC,
    EVENT,
    DATA_SCAN_COLLECTION,
    PROXIMITY_DETECTION,
    SMS,
    UNKNOWN
}
```

**Complete list of enums to implement:**
- MessageType ✓
- RawPositionType
- BatteryStatus
- DynamicMotionState
- TimeoutCause
- BleBeaconFailure
- EventType
- Mode (tracking modes)
- DebugCommandType
- ShutdownCause
- GpsFixStatus
- ErrorCode
- AngleDetectionControl
- MiscDataTag
- OptionalCommand
- ResetAction
- MelodyId
- DownMessageType
- CollectionScanType
- BleBondStatus

### Phase 3: Core Data Models

#### 3.1 Main API Classes
```java
// AbeewaySmartBadgeCodec.java - Main entry point
public class AbeewaySmartBadgeCodec {
    
    public DecodedUplink decodeUplink(byte[] bytes, int fPort, String timestamp) 
        throws DecodingException {
        // Main decoding logic
    }
    
    public EncodedDownlink encodeDownlink(Object data, int fPort) 
        throws EncodingException {
        // Main encoding logic
    }
    
    public DecodedDownlink decodeDownlink(byte[] bytes, int fPort) 
        throws DecodingException {
        // Downlink decoding for verification
    }
}
```

#### 3.2 Core Data Classes
```java
// models/DecodedUplink.java
public class DecodedUplink {
    private Object data;              // Main decoded data
    private List<String> errors;      // Error messages
    private List<String> warnings;    // Warning messages
    
    // constructors, getters, setters
}

// models/UplinkData.java - The actual payload data
public class UplinkData {
    private MessageType messageType;
    private String payload;           // Hex string of original payload
    private Integer ackToken;
    private String trackingMode;
    private Boolean onDemand;
    private Boolean periodicPosition;
    private String dynamicMotionState;
    private Integer appState;
    private Integer sosFlag;
    private Integer batteryLevel;
    private String batteryStatus;
    private Double batteryVoltage;
    private Double temperatureMeasure;
    private Map<String, Object> deviceConfiguration;
    
    // Position-related fields
    private String rawPositionType;
    private Double gpsLatitude;
    private Double gpsLongitude;
    private Object horizontalAccuracy;
    private Integer age;
    
    // All other fields from JavaScript version...
}
```

### Phase 4: Utility Classes

#### 4.1 Bit Manipulation Utilities
```java
// utils/BitUtils.java
public class BitUtils {
    
    public static int extractBits(int value, int start, int length) {
        int mask = (1 << length) - 1;
        return (value >> start) & mask;
    }
    
    public static boolean getBit(int value, int position) {
        return (value & (1 << position)) != 0;
    }
    
    public static double scaleValue(int rawValue, double min, double max, 
                                  int bits, double offset) {
        return (rawValue - offset / 2) / 
               (((1 << bits) - 1 - offset) / (max - min)) + min;
    }
    
    // More utility methods...
}
```

#### 4.2 Byte Array Utilities  
```java
// utils/ByteUtils.java
public class ByteUtils {
    
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b & 0xFF));
        }
        return result.toString();
    }
    
    public static int bytesToInt(byte[] bytes, int offset, int length) {
        int result = 0;
        for (int i = 0; i < length; i++) {
            result = (result << 8) | (bytes[offset + i] & 0xFF);
        }
        return result;
    }
    
    public static short signedShort(int value) {
        return (short) (value > 32767 ? value - 65536 : value);
    }
    
    // More utility methods...
}
```

### Phase 5: Message Type Decoders

#### 5.1 Position Message Decoder
```java
// decoders/PositionDecoder.java
public class PositionDecoder {
    
    public void decodePositionMessage(UplinkData data, byte[] payload) {
        data.setRawPositionType(determineRawPositionType(payload));
        
        switch (RawPositionType.valueOf(data.getRawPositionType())) {
            case GPS:
                decodeGpsPosition(data, payload, MessageType.POSITION_MESSAGE);
                break;
            case GPS_TIMEOUT:
                decodeGpsTimeout(data, payload, MessageType.POSITION_MESSAGE);
                break;
            case WIFI_BSSIDS_WITH_NO_CYPHER:
                decodeWifiPosition(data, payload, MessageType.POSITION_MESSAGE);
                break;
            case BLE_BEACON_SCAN:
                decodeBlePosition(data, payload, MessageType.POSITION_MESSAGE);
                break;
            // ... all other position types
        }
    }
    
    private void decodeGpsPosition(UplinkData data, byte[] payload, MessageType msgType) {
        data.setAge(determineAge(payload));
        data.setGpsLatitude(determineLatitude(payload, msgType));
        data.setGpsLongitude(determineLongitude(payload, msgType));
        data.setHorizontalAccuracy(determineHorizontalAccuracy(payload, msgType));
    }
    
    // Implement all helper methods from JavaScript...
}
```

#### 5.2 Configuration Decoder/Encoder
```java
// decoders/ConfigurationDecoder.java
public class ConfigurationDecoder {
    
    private final Map<Integer, Parameter> parametersById;
    private final Map<String, Parameter> parametersByName;
    
    public void decodeConfiguration(UplinkData data, byte[] payload) {
        // Port the complex parameter decoding logic
        // Handle all 200+ parameters with their specific types:
        // - ParameterTypeNumber (with ranges, multipliers)
        // - ParameterTypeString (with firmware/driver value mappings)
        // - ParameterTypeBitMap (complex bit field structures)
    }
    
    // Encoding methods for downlinks...
}
```

### Phase 6: Main Decoder Implementation

#### 6.1 Core Decoding Logic
```java
// AbeewaySmartBadgeCodec.java implementation
public DecodedUplink decodeUplink(byte[] bytes, int fPort, String timestamp) {
    try {
        UplinkData data = new UplinkData();
        
        // Step 1: Determine message type
        MessageType messageType = determineMessageType(bytes);
        data.setMessageType(messageType);
        
        // Step 2: Extract common fields (if not FRAME_PENDING or SMS)
        if (messageType != MessageType.FRAME_PENDING && 
            messageType != MessageType.SMS) {
            extractCommonFields(data, bytes, fPort);
        }
        
        // Step 3: Message-specific decoding
        switch (messageType) {
            case POSITION_MESSAGE:
                positionDecoder.decodePositionMessage(data, bytes);
                break;
            case EXTENDED_POSITION_MESSAGE:
                positionDecoder.decodeExtendedPositionMessage(data, bytes);
                break;
            case CONFIGURATION:
                configurationDecoder.decodeConfiguration(data, bytes);
                break;
            case EVENT:
                eventDecoder.decodeEvent(data, bytes);
                break;
            // ... all 19 message types
        }
        
        return new DecodedUplink(data, Collections.emptyList(), Collections.emptyList());
        
    } catch (Exception e) {
        return new DecodedUplink(null, List.of(e.getMessage()), Collections.emptyList());
    }
}
```

### Phase 7: Encoding Implementation

#### 7.1 Downlink Encoding
```java
// encoders/CommandEncoder.java
public class CommandEncoder {
    
    public byte[] encodeDownlink(Map<String, Object> data) {
        String messageType = (String) data.get("messageType");
        
        switch (messageType) {
            case "REQUEST_CONFIG":
                return encodeConfigRequest(data);
            case "SET_MODE":
                return encodeModeSet(data);
            case "DEBUG":
                return encodeDebugCommand(data);
            case "POSITION_ON_DEMAND":
                return encodePositionRequest(data);
            // ... all downlink types
        }
    }
    
    // Implement all encoding methods...
}
```

### Phase 8: Testing Strategy

#### 8.1 Comprehensive Test Suite
```java
// test/AbeewayCodecTest.java
@TestMethodOrder(OrderAnnotation.class)
public class AbeewayCodecTest {
    
    private AbeewaySmartBadgeCodec codec;
    private List<TestExample> examples;
    
    @BeforeEach
    void setUp() throws IOException {
        codec = new AbeewaySmartBadgeCodec();
        // Load examples.json with 390+ test cases
        examples = loadTestExamples();
    }
    
    @Test
    @Order(1)
    void testAllUplinkExamples() {
        for (TestExample example : examples) {
            if ("uplink".equals(example.getType())) {
                testUplinkExample(example);
            }
        }
    }
    
    @Test
    @Order(2) 
    void testAllDownlinkEncodeExamples() {
        for (TestExample example : examples) {
            if ("downlink-encode".equals(example.getType())) {
                testDownlinkEncodeExample(example);
            }
        }
    }
    
    @Test
    @Order(3)
    void testAllDownlinkDecodeExamples() {
        for (TestExample example : examples) {
            if ("downlink-decode".equals(example.getType())) {
                testDownlinkDecodeExample(example);
            }
        }
    }
}
```

## Critical Implementation Details

### 1. Exact Bit-Level Compatibility
The JavaScript driver performs complex bit manipulations. Every bit operation must be replicated exactly:

```java
// Example: GPS coordinate decoding
private double determineLatitude(byte[] payload, MessageType messageType) {
    int startIdx, endIdx;
    String padding = "";
    
    switch (messageType) {
        case EXTENDED_POSITION_MESSAGE:
            startIdx = 8; endIdx = 12; padding = "";
            break;
        case POSITION_MESSAGE:
            startIdx = 6; endIdx = 9; padding = "00";
            break;
        default:
            throw new IllegalArgumentException("Invalid message type");
    }
    
    byte[] coordBytes = Arrays.copyOfRange(payload, startIdx, endIdx);
    String hexStr = ByteUtils.bytesToHex(coordBytes) + padding;
    long rawValue = Long.parseUnsignedLong(hexStr, 16);
    
    // Handle signed conversion
    if (rawValue > 2147483647L) {
        rawValue -= 4294967296L;
    }
    
    return rawValue / Math.pow(10, 7);
}
```

### 2. Parameter Management System
The configuration system is extremely complex with 200+ parameters. You must:

1. **Load parameter definitions** from `parameters.json`
2. **Implement type-specific handling**:
   - ParameterTypeNumber: ranges, multipliers, signed/unsigned
   - ParameterTypeString: firmware-to-driver value mapping
   - ParameterTypeBitMap: complex bit field structures
3. **Handle parameter validation** exactly as JavaScript does

### 3. Error Handling Strategy
Match JavaScript error handling patterns:
- **DecodingException** for malformed payloads
- **EncodingException** for invalid downlink data
- **ValidationException** for parameter range violations

## Implementation Timeline

**Phase 1-2 (Week 1)**: Project setup, enums, basic data models
**Phase 3-4 (Week 2)**: Utility classes, basic decoders
**Phase 5-6 (Week 3)**: Message decoders, main codec logic  
**Phase 7 (Week 4)**: Encoding implementation
**Phase 8 (Week 5)**: Testing and validation with all 390+ examples

## Quality Assurance

### Must-Pass Criteria:
1. **All 390+ test examples pass** with identical output to JavaScript
2. **Bit-perfect encoding/decoding** for all message types
3. **Complete parameter coverage** (all 200+ parameters)
4. **Error handling parity** with JavaScript version

### Testing Strategy:
1. **Start with simple messages** (HEARTBEAT, basic GPS)
2. **Progress to complex messages** (configuration, events)
3. **Validate against ALL examples** continuously
4. **Cross-reference with original** JavaScript output

## Integration Notes

### Library Usage:
```java
// In your consuming application
AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();

// Decode uplink from LoRaWAN
DecodedUplink result = codec.decodeUplink(payloadBytes, fPort, timestamp);
UplinkData data = (UplinkData) result.getData();

// Access GPS coordinates
if (data.getGpsLatitude() != null) {
    double lat = data.getGpsLatitude();
    double lon = data.getGpsLongitude();
}

// Check battery status
if (data.getBatteryLevel() != null) {
    int batteryPercent = data.getBatteryLevel();
}

// Handle button press events
if (data.getMessageType() == MessageType.EVENT) {
    // Process event data
}

// Encode configuration downlink
Map<String, Object> configCommand = Map.of(
    "messageType", "SET_MODE",
    "mode", "MOTION_TRACKING"
);
EncodedDownlink downlink = codec.encodeDownlink(configCommand, 2);
```

## Success Metrics

Your implementation is complete when:
- ✅ All 390+ examples pass with identical JSON output
- ✅ All message types decode correctly  
- ✅ All downlink commands encode correctly
- ✅ Parameter system handles all 200+ parameters
- ✅ Error cases match JavaScript behavior
- ✅ Performance is acceptable for your use case

This will give you a **production-ready, full-featured Java library** with complete compatibility with the Abeeway Smart Badge JavaScript driver.