package com.abeeway.smartbadge;

import com.abeeway.smartbadge.exceptions.DecodingException;
import com.abeeway.smartbadge.exceptions.EncodingException;
import com.abeeway.smartbadge.models.DecodedUplink;
import com.abeeway.smartbadge.models.EncodedDownlink;
import com.abeeway.smartbadge.models.DecodedDownlink;
import com.abeeway.smartbadge.models.UplinkData;
import com.abeeway.smartbadge.enums.MessageType;
import com.abeeway.smartbadge.decoders.*;
import com.abeeway.smartbadge.encoders.CommandEncoder;
import com.abeeway.smartbadge.utils.BitUtils;
import com.abeeway.smartbadge.utils.ByteUtils;
import com.abeeway.smartbadge.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Main API class for the Abeeway Smart Badge Codec library.
 * Provides functionality to decode uplink messages and encode downlink commands
 * for Abeeway Smart Badge LoRaWAN devices.
 */
public class AbeewaySmartBadgeCodec {
    
    private final PositionDecoder positionDecoder;
    private final ConfigurationDecoder configurationDecoder;
    private final EventDecoder eventDecoder;
    private final CommonFieldsDecoder commonFieldsDecoder;
    private final CommandEncoder commandEncoder;
    
    public AbeewaySmartBadgeCodec() {
        this.positionDecoder = new PositionDecoder();
        this.configurationDecoder = new ConfigurationDecoder();
        this.eventDecoder = new EventDecoder();
        this.commonFieldsDecoder = new CommonFieldsDecoder();
        this.commandEncoder = new CommandEncoder();
        
        // Initialize configuration decoder parameters
        this.configurationDecoder.initializeParameters();
    }

    /**
     * Decodes an uplink message from an Abeeway Smart Badge device.
     *
     * @param bytes     The raw payload bytes
     * @param fPort     The LoRaWAN frame port
     * @param timestamp ISO timestamp string (optional)
     * @return DecodedUplink containing the decoded data and any errors/warnings
     * @throws DecodingException if the payload cannot be decoded
     */
    public DecodedUplink decodeUplink(byte[] bytes, int fPort, String timestamp) 
            throws DecodingException {
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        try {
            // Validate input parameters
            if (bytes == null || bytes.length == 0) {
                throw new DecodingException("Empty or null payload");
            }
            
            if (!ValidationUtils.isValidFramePort(fPort)) {
                warnings.add("Invalid frame port: " + fPort);
            }
            
            // Create uplink data container
            UplinkData data = new UplinkData();
            data.setPayload(ByteUtils.bytesToHex(bytes));
            
            // Step 1: Determine message type
            MessageType messageType = determineMessageType(bytes);
            data.setMessageType(messageType);
            
            // Step 2: Extract common fields (except for special message types)
            if (messageType != MessageType.FRAME_PENDING && 
                messageType != MessageType.SMS) {
                commonFieldsDecoder.extractCommonFields(data, bytes, fPort);
            }
            
            // Step 3: Message-specific decoding
            switch (messageType) {
                case POSITION_MESSAGE:
                    positionDecoder.decodePositionMessage(data, bytes);
                    break;
                    
                case EXTENDED_POSITION_MESSAGE:
                    positionDecoder.decodeExtendedPositionMessage(data, bytes);
                    break;
                    
                case HEARTBEAT:
                    decodeHeartbeat(data, bytes);
                    break;
                    
                case ENERGY_STATUS:
                    decodeEnergyStatus(data, bytes);
                    break;
                    
                case HEALTH_STATUS:
                    decodeHealthStatus(data, bytes);
                    break;
                    
                case CONFIGURATION:
                    configurationDecoder.decodeConfiguration(data, bytes);
                    break;
                    
                case EVENT:
                    eventDecoder.decodeEvent(data, bytes);
                    break;
                    
                case ACTIVITY_STATUS:
                    decodeActivityStatus(data, bytes);
                    break;
                    
                case SHUTDOWN:
                    decodeShutdown(data, bytes);
                    break;
                    
                case DEBUG:
                    decodeDebug(data, bytes);
                    break;
                    
                case SHOCK_DETECTION:
                    decodeShockDetection(data, bytes);
                    break;
                    
                case BLE_MAC:
                    decodeBleMAC(data, bytes);
                    break;
                    
                case DATA_SCAN_COLLECTION:
                    decodeDataScanCollection(data, bytes);
                    break;
                    
                case PROXIMITY_DETECTION:
                    decodeProximityDetection(data, bytes);
                    break;
                    
                case SMS:
                    decodeSMS(data, bytes);
                    break;
                    
                case FRAME_PENDING:
                    // Frame pending messages typically have no additional data
                    break;
                    
                default:
                    warnings.add("Unknown message type: " + messageType);
                    break;
            }
            
            return new DecodedUplink(data, errors, warnings);
            
        } catch (Exception e) {
            throw new DecodingException("Failed to decode uplink message: " + e.getMessage(), e);
        }
    }
    
    /**
     * Determine the message type from the payload.
     */
    private MessageType determineMessageType(byte[] bytes) {
        if (bytes.length == 0) {
            return MessageType.UNKNOWN;
        }
        
        int firstByte = ByteUtils.toUnsigned(bytes[0]);
        int messageTypeCode = BitUtils.extractBits(firstByte, 4, 4);
        
        switch (messageTypeCode) {
            case 0: return MessageType.POSITION_MESSAGE;
            case 1: return MessageType.EXTENDED_POSITION_MESSAGE;
            case 2: return MessageType.HEARTBEAT;
            case 3: return MessageType.ENERGY_STATUS;
            case 4: return MessageType.HEALTH_STATUS;
            case 5: return MessageType.SHUTDOWN;
            case 6: return MessageType.FRAME_PENDING;
            case 7: return MessageType.DEBUG;
            case 8: return MessageType.ACTIVITY_STATUS;
            case 9: return MessageType.CONFIGURATION;
            case 10: return MessageType.SHOCK_DETECTION;
            case 11: return MessageType.BLE_MAC;
            case 12: return MessageType.EVENT;
            case 13: return MessageType.DATA_SCAN_COLLECTION;
            case 14: return MessageType.PROXIMITY_DETECTION;
            case 15: return MessageType.SMS;
            default: return MessageType.UNKNOWN;
        }
    }
    
    // Simple decoders for other message types
    private void decodeHeartbeat(UplinkData data, byte[] bytes) {
        // Heartbeat is typically just common fields
        commonFieldsDecoder.extractTemperature(data, bytes, bytes.length - 1);
    }
    
    private void decodeEnergyStatus(UplinkData data, byte[] bytes) {
        // Extract detailed battery information
        if (bytes.length >= 2) {
            int energyByte = ByteUtils.toUnsigned(bytes[1]);
            data.setBatteryLevel(energyByte);
        }
    }
    
    private void decodeHealthStatus(UplinkData data, byte[] bytes) {
        // Extract health and diagnostic information
        commonFieldsDecoder.extractTemperature(data, bytes, 1);
    }
    
    private void decodeActivityStatus(UplinkData data, byte[] bytes) {
        // Extract activity and step count information
        if (bytes.length >= 3) {
            int stepCount = ByteUtils.bytesToInt(bytes, 1, 2);
            data.setStepCount(stepCount);
        }
    }
    
    private void decodeShutdown(UplinkData data, byte[] bytes) {
        // Extract shutdown cause and related information
        // Implementation would depend on specific payload format
    }
    
    private void decodeDebug(UplinkData data, byte[] bytes) {
        // Extract debug information
        if (bytes.length >= 2) {
            String debugData = ByteUtils.bytesToHex(bytes).substring(2);
            data.setDebugData(debugData);
        }
    }
    
    private void decodeShockDetection(UplinkData data, byte[] bytes) {
        // Extract shock detection information
        data.setMotionDetected(true);
    }
    
    private void decodeBleMAC(UplinkData data, byte[] bytes) {
        // Extract BLE MAC addresses
        // Implementation would depend on specific payload format
    }
    
    private void decodeDataScanCollection(UplinkData data, byte[] bytes) {
        // Extract scan collection data
        // Implementation would depend on specific payload format
    }
    
    private void decodeProximityDetection(UplinkData data, byte[] bytes) {
        // Extract proximity detection data
        // Implementation would depend on specific payload format
    }
    
    private void decodeSMS(UplinkData data, byte[] bytes) {
        // Extract SMS data
        // Implementation would depend on specific payload format
    }

    /**
     * Encodes a downlink command for an Abeeway Smart Badge device.
     *
     * @param data  The command data to encode
     * @param fPort The LoRaWAN frame port
     * @return EncodedDownlink containing the encoded bytes
     * @throws EncodingException if the data cannot be encoded
     */
    @SuppressWarnings("unchecked")
    public EncodedDownlink encodeDownlink(Object data, int fPort) 
            throws EncodingException {
        
        if (data == null) {
            throw new EncodingException("Downlink data cannot be null");
        }
        
        if (!(data instanceof Map)) {
            throw new EncodingException("Downlink data must be a Map");
        }
        
        Map<String, Object> commandData = (Map<String, Object>) data;
        return commandEncoder.encodeDownlink(commandData, fPort);
    }

    /**
     * Decodes a downlink message for verification purposes.
     *
     * @param bytes The raw payload bytes
     * @param fPort The LoRaWAN frame port
     * @return DecodedDownlink containing the decoded command data
     * @throws DecodingException if the payload cannot be decoded
     */
    public DecodedDownlink decodeDownlink(byte[] bytes, int fPort) 
            throws DecodingException {
        // TODO: Implement downlink decoding logic in Phase 7
        throw new UnsupportedOperationException("Downlink decoding not yet implemented");
    }
}