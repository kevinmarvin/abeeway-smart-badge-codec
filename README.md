# Abeeway Smart Badge Java Codec

Java library for decoding/encoding Abeeway Smart Badge LoRaWAN messages with complete feature parity to the JavaScript driver.

## Features

- Complete decoding of all 19 message types
- GPS/WiFi/BLE position decoding
- Device configuration management (200+ parameters)
- Event handling (button presses, motion, SOS, etc.)
- Battery and health status monitoring
- Downlink command encoding
- Full compatibility with JavaScript driver test cases

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

```java
AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();

// Decode uplink message
DecodedUplink result = codec.decodeUplink(payloadBytes, fPort, timestamp);

// Encode downlink command
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