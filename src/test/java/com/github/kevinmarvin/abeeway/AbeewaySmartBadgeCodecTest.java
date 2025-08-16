package com.github.kevinmarvin.abeeway;

import com.github.kevinmarvin.abeeway.exceptions.DecodingException;
import com.github.kevinmarvin.abeeway.exceptions.EncodingException;
import com.github.kevinmarvin.abeeway.models.DecodedUplink;
import com.github.kevinmarvin.abeeway.models.EncodedDownlink;
import com.github.kevinmarvin.abeeway.models.UplinkData;
import com.github.kevinmarvin.abeeway.enums.MessageType;
import com.github.kevinmarvin.abeeway.utils.ByteUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test suite for the Abeeway Smart Badge Codec library.
 * Tests validate basic functionality and will eventually validate against 
 * the 390+ examples from the JavaScript driver.
 */
@TestMethodOrder(OrderAnnotation.class)
public class AbeewaySmartBadgeCodecTest {

    private AbeewaySmartBadgeCodec codec;

    @BeforeEach
    void setUp() {
        codec = new AbeewaySmartBadgeCodec();
    }

    @Test
    @Order(1)
    void testBasicInstantiation() {
        assertNotNull(codec, "Codec should be instantiated successfully");
    }
    
    @Test
    @Order(2)
    void testEmptyPayloadHandling() {
        assertThrows(DecodingException.class, () -> {
            codec.decodeUplink(null, 1, null);
        }, "Should throw DecodingException for null payload");
        
        assertThrows(DecodingException.class, () -> {
            codec.decodeUplink(new byte[0], 1, null);
        }, "Should throw DecodingException for empty payload");
    }
    
    @Test
    @Order(3)
    void testBasicHeartbeatDecoding() throws DecodingException {
        // Simple heartbeat message: message type 2 (bits 4-7) = 0x20
        byte[] heartbeatPayload = {(byte) 0x20, 0x50}; // Heartbeat with some data
        
        DecodedUplink result = codec.decodeUplink(heartbeatPayload, 1, null);
        
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData() instanceof UplinkData);
        
        UplinkData data = (UplinkData) result.getData();
        assertEquals(MessageType.HEARTBEAT, data.getMessageType());
        assertEquals("2050", data.getPayload());
    }
    
    @Test
    @Order(4)
    void testBasicPositionDecoding() throws DecodingException {
        // Simple position message: message type 0 (bits 4-7) = 0x00
        // GPS position type 0 (bits 0-3) = 0x00
        byte[] positionPayload = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        
        DecodedUplink result = codec.decodeUplink(positionPayload, 1, null);
        
        assertNotNull(result);
        UplinkData data = (UplinkData) result.getData();
        assertEquals(MessageType.POSITION_MESSAGE, data.getMessageType());
    }
    
    @Test
    @Order(5)
    void testBasicEventDecoding() throws DecodingException {
        // Event message: message type 12 (0xC0) with button press event
        byte[] eventPayload = {(byte) 0xC0, 0x01}; // Event message with button press
        
        DecodedUplink result = codec.decodeUplink(eventPayload, 1, null);
        
        assertNotNull(result);
        UplinkData data = (UplinkData) result.getData();
        assertEquals(MessageType.EVENT, data.getMessageType());
        assertNotNull(data.getEventType());
    }
    
    @Test
    @Order(6)
    void testDownlinkEncoding() throws EncodingException {
        Map<String, Object> commandData = new HashMap<>();
        commandData.put("messageType", "REQUEST_CONFIG");
        
        EncodedDownlink result = codec.encodeDownlink(commandData, 2);
        
        assertNotNull(result);
        assertNotNull(result.getBytes());
        assertTrue(result.getBytes().length > 0);
        assertEquals(2, result.getfPort());
    }
    
    @Test
    @Order(7)
    void testModeSetEncoding() throws EncodingException {
        Map<String, Object> commandData = new HashMap<>();
        commandData.put("messageType", "SET_MODE");
        commandData.put("mode", "MOTION_TRACKING");
        
        EncodedDownlink result = codec.encodeDownlink(commandData, 2);
        
        assertNotNull(result);
        byte[] bytes = result.getBytes();
        assertEquals(2, bytes.length); // Command byte + mode byte
        assertEquals(0x02, bytes[0]); // SET_MODE command
        assertEquals(0x02, bytes[1]); // MOTION_TRACKING mode
    }
    
    @Test
    @Order(8)
    void testInvalidEncodingData() {
        // Test null data
        assertThrows(EncodingException.class, () -> {
            codec.encodeDownlink(null, 2);
        });
        
        // Test non-Map data
        assertThrows(EncodingException.class, () -> {
            codec.encodeDownlink("invalid", 2);
        });
        
        // Test missing message type
        Map<String, Object> commandData = new HashMap<>();
        assertThrows(EncodingException.class, () -> {
            codec.encodeDownlink(commandData, 2);
        });
    }
    
    @Test
    @Order(9)
    void testUtilityMethods() {
        // Test ByteUtils
        byte[] testBytes = {0x12, 0x34, (byte) 0xAB, (byte) 0xCD};
        String hex = ByteUtils.bytesToHex(testBytes);
        assertEquals("1234ABCD", hex);
        
        byte[] parsedBytes = ByteUtils.hexToBytes(hex);
        assertArrayEquals(testBytes, parsedBytes);
        
        // Test integer conversion
        int intValue = ByteUtils.bytesToInt(testBytes, 0, 4);
        assertEquals(0x1234ABCD, intValue);
    }
    
    @Test
    @Order(10) 
    void testMessageTypeDetection() throws DecodingException {
        // Test various message types
        byte[] heartbeat = {(byte) 0x20}; // Message type 2
        DecodedUplink result1 = codec.decodeUplink(heartbeat, 1, null);
        assertEquals(MessageType.HEARTBEAT, ((UplinkData) result1.getData()).getMessageType());
        
        byte[] config = {(byte) 0x90}; // Message type 9
        DecodedUplink result2 = codec.decodeUplink(config, 1, null);
        assertEquals(MessageType.CONFIGURATION, ((UplinkData) result2.getData()).getMessageType());
        
        byte[] event = {(byte) 0xC0}; // Message type 12
        DecodedUplink result3 = codec.decodeUplink(event, 1, null);
        assertEquals(MessageType.EVENT, ((UplinkData) result3.getData()).getMessageType());
    }
    
    // Future test methods would include:
    // - testAllUplinkExamples() - Load from examples.json and validate all test cases
    // - testAllDownlinkEncodeExamples() - Test all downlink encoding scenarios
    // - testComplexPositionDecoding() - Test GPS, WiFi, BLE position decoding
    // - testConfigurationDecoding() - Test parameter decoding
    // - testErrorHandling() - Test various error conditions
    // - testPerformance() - Performance benchmarks
}