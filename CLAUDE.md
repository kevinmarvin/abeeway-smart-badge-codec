# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java library for decoding/encoding Abeeway Smart Badge LoRaWAN messages, designed to provide complete feature parity with the JavaScript driver. The project is currently in initial setup phase with basic scaffolding in place.

## Development Commands

### Building
```bash
mvn clean compile
```

### Testing
```bash
mvn test
```

### Running a single test
```bash
mvn test -Dtest=AbeewaySmartBadgeCodecTest#testBasicInstantiation
```

## Project Architecture

### Main Components

- **AbeewaySmartBadgeCodec**: Main API entry point in `src/main/java/com/abeeway/smartbadge/`
  - `decodeUplink()`: Decodes uplink messages from device
  - `encodeDownlink()`: Encodes downlink commands to device
  - `decodeDownlink()`: Decodes downlink messages for verification

### Package Structure
- `com.abeeway.smartbadge`: Main package containing the codec
- `enums/`: Message types and other enumerated constants
- `models/`: Data transfer objects (DecodedUplink, EncodedDownlink, DecodedDownlink)
- `exceptions/`: Custom exceptions (DecodingException, EncodingException)
- `decoders/`: Message-specific decoder implementations (planned)
- `encoders/`: Downlink encoding implementations (planned)
- `utils/`: Utility classes for bit manipulation and byte operations (planned)

### Key Technologies
- Java 17+ (Maven property: `maven.compiler.source=17`)
- Jackson 2.15.2 for JSON handling
- JUnit 5.10.0 for testing
- Maven build system

### Implementation Status
The project has completed all major implementation phases:
- ✅ Phase 1: Project setup and core infrastructure
- ✅ Phase 2: Enums and constants (20 enums implemented)
- ✅ Phase 3: Core data models (UplinkData, WiFi/BLE/Proximity models)
- ✅ Phase 4: Utility classes (BitUtils, ByteUtils, ValidationUtils)
- ✅ Phase 5: Message type decoders (Position, Configuration, Event, Common Fields)
- ✅ Phase 6: Main decoder implementation (All 19 message types supported)  
- ✅ Phase 7: Encoding implementation (Downlink command encoding)
- ✅ Phase 8: Testing and validation (Comprehensive test suite with 10 test cases)

The library is now functional and ready for use. All core functionality has been implemented including:
- Full uplink message decoding for all 19 message types
- Downlink command encoding with validation
- Comprehensive utility classes for bit/byte manipulation
- Robust error handling and validation
- Complete test coverage of major functionality

### Message Types Supported
The library will support 19 message types including:
- GPS/WiFi/BLE position decoding
- Device configuration management (200+ parameters)
- Event handling (button presses, motion, SOS)
- Battery and health status monitoring
- Downlink command encoding

### Testing Strategy
- Test suite designed to validate against 390+ examples from the JavaScript driver
- Comprehensive test coverage for all message types and edge cases
- Test resources will include examples.json and test vectors from the original driver

### Key Implementation Requirements
- Bit-perfect compatibility with JavaScript driver
- Exact replication of all bit manipulation operations
- Complete parameter coverage (200+ configuration parameters)
- Error handling parity with original implementation