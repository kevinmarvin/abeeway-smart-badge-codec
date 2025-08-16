# Abeeway Smart Badge Java Codec

Java library for decoding/encoding Abeeway Smart Badge LoRaWAN messages with complete feature parity to the JavaScript driver.

## Features

### Core Functionality
- Complete decoding of all 19 message types
- GPS/WiFi/BLE position decoding
- Device configuration management (200+ parameters)
- Event handling (button presses, motion, SOS, etc.)
- Battery and health status monitoring
- Downlink command encoding
- Full compatibility with JavaScript driver test cases

### 🚀 High-Level Parameter Builder (NEW!)
- **Fluent API**: Configure devices without protocol knowledge
- **Type Safety**: Compile-time validation of parameter types  
- **Auto Validation**: Parameter ranges validated automatically
- **Self-Documenting**: Human-readable parameter names
- **Parameter Discovery**: Find available parameters with descriptions
- **IDE Support**: Full autocomplete and documentation

```java
// Easy configuration - no protocol expertise needed!
AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
    .setGpsTimeout(120)
    .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
    .setBatteryLowThreshold(15)
    .build();
```

## Requirements

- Java 17+
- Maven 3.6+

## Building

```bash
mvn clean compile
```

## Testing

```bash
mvn test
```

## Usage

### Basic Decoding

```java
AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();

// Decode uplink message
DecodedUplink result = codec.decodeUplink(payloadBytes, fPort, timestamp);
UplinkData data = (UplinkData) result.getData();
```

## Installation

### Option 1: JitPack (Recommended - Zero Setup)

Add to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.kevinmarvin</groupId>
        <artifactId>abeeway-smart-badge-codec</artifactId>
        <version>v1.0.0</version>
    </dependency>
</dependencies>
```

### Option 2: Local Installation

```bash
git clone https://github.com/kevinmarvin/abeeway-smart-badge-codec.git
cd abeeway-smart-badge-codec
mvn clean install
```

Then add to your project:
```xml
<dependency>
    <groupId>com.github.kevinmarvin</groupId>
    <artifactId>abeeway-smart-badge-codec</artifactId>
    <version>1.0.0</version>
</dependency>
```

### High-Level Parameter Configuration (Recommended)

```java
import com.github.kevinmarvin.abeeway.AbeewaySmartBadgeCodec;
import com.github.kevinmarvin.abeeway.parameters.AbeewayParams;

// Build parameters using fluent API - no protocol knowledge required!
AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
    .setGpsTimeout(120)                    // 2 minutes
    .setUplinkPeriod(3600)                // 1 hour
    .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
    .setBatteryLowThreshold(15)           // 15%
    .setPowerSaveMode(true)
    .build();

// Encode parameters for transmission
EncodedDownlink downlink = codec.encodeParameters(params, 2);
```

### Configuration Commands

```java
import com.abeeway.smartbadge.encoders.ParameterEncoder;

// Set device mode
EncodedDownlink modeCommand = codec.encodeConfigurationCommand(
    ParameterEncoder.setMode(AbeewayParams.OperatingMode.MOTION_TRACKING), 2);

// Request current configuration
EncodedDownlink configRequest = codec.encodeConfigurationCommand(
    ParameterEncoder.requestConfiguration(), 2);
```

### Parameter Discovery

```java
// Get all available parameters with their descriptions and ranges
Map<String, String> availableParams = codec.getAvailableParameters();
for (Map.Entry<String, String> param : availableParams.entrySet()) {
    System.out.println(param.getKey() + ": " + param.getValue());
}
```

### Low-Level Encoding (Advanced)

```java
// For advanced users who know the protocol details
Map<String, Object> commandData = Map.of("messageType", "SET_MODE", "mode", "MOTION_TRACKING");
EncodedDownlink downlink = codec.encodeDownlink(commandData, fPort);
```

## Project Status

This project has completed all major implementation phases as outlined in IMPLEMENTATION.md:

- ✅ Phase 1: Project setup and core infrastructure
- ✅ Phase 2: Enums and constants (20 enums implemented)
- ✅ Phase 3: Core data models (UplinkData, WiFi/BLE/Proximity models)
- ✅ Phase 4: Utility classes (BitUtils, ByteUtils, ValidationUtils)
- ✅ Phase 5: Message type decoders (Position, Configuration, Event, Common Fields)
- ✅ Phase 6: Main decoder implementation (All 19 message types supported)
- ✅ Phase 7: Encoding implementation (Downlink command encoding)
- ✅ Phase 8: Testing and validation (10 comprehensive test cases)

The library is now functional and ready for basic use. All tests are passing and the core functionality is implemented.