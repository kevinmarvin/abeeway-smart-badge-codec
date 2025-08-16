package com.abeeway.smartbadge.encoders;

import com.abeeway.smartbadge.exceptions.EncodingException;
import com.abeeway.smartbadge.models.EncodedDownlink;
import com.abeeway.smartbadge.utils.ByteUtils;

import java.util.Map;

/**
 * Encoder for downlink commands to Abeeway Smart Badge devices.
 */
public class CommandEncoder {
    
    /**
     * Encode a downlink command.
     */
    public EncodedDownlink encodeDownlink(Map<String, Object> data, int fPort) throws EncodingException {
        String messageType = (String) data.get("messageType");
        if (messageType == null) {
            throw new EncodingException("Missing messageType in downlink data");
        }
        
        byte[] encodedBytes;
        
        switch (messageType) {
            case "REQUEST_CONFIG":
                encodedBytes = encodeConfigRequest(data);
                break;
            case "SET_MODE":
                encodedBytes = encodeModeSet(data);
                break;
            case "DEBUG":
                encodedBytes = encodeDebugCommand(data);
                break;
            case "POSITION_ON_DEMAND":
                encodedBytes = encodePositionRequest(data);
                break;
            case "SET_PARAM":
                encodedBytes = encodeSetParameter(data);
                break;
            default:
                throw new EncodingException("Unknown downlink message type: " + messageType);
        }
        
        return new EncodedDownlink(encodedBytes, fPort);
    }
    
    /**
     * Encode configuration request.
     */
    private byte[] encodeConfigRequest(Map<String, Object> data) {
        // Simple config request - typically just a command byte
        return new byte[]{0x01};
    }
    
    /**
     * Encode mode set command.
     */
    private byte[] encodeModeSet(Map<String, Object> data) {
        String mode = (String) data.get("mode");
        if (mode == null) {
            throw new IllegalArgumentException("Missing mode parameter");
        }
        
        byte modeCode = encodeModeToCode(mode);
        return new byte[]{0x02, modeCode};
    }
    
    /**
     * Encode debug command.
     */
    private byte[] encodeDebugCommand(Map<String, Object> data) {
        Object debugCmd = data.get("debugCommand");
        if (debugCmd == null) {
            throw new IllegalArgumentException("Missing debugCommand parameter");
        }
        
        byte[] debugBytes = ByteUtils.hexToBytes(debugCmd.toString());
        byte[] result = new byte[debugBytes.length + 1];
        result[0] = 0x07; // Debug message type
        System.arraycopy(debugBytes, 0, result, 1, debugBytes.length);
        return result;
    }
    
    /**
     * Encode position request.
     */
    private byte[] encodePositionRequest(Map<String, Object> data) {
        // Simple position request
        return new byte[]{0x04};
    }
    
    /**
     * Encode set parameter command.
     */
    private byte[] encodeSetParameter(Map<String, Object> data) {
        Object paramId = data.get("parameterId");
        Object paramValue = data.get("parameterValue");
        
        if (paramId == null || paramValue == null) {
            throw new IllegalArgumentException("Missing parameter ID or value");
        }
        
        int id = ((Number) paramId).intValue();
        int value = ((Number) paramValue).intValue();
        
        return new byte[]{
            0x09, // Set parameter command
            (byte) id,
            (byte) (value & 0xFF),
            (byte) ((value >> 8) & 0xFF)
        };
    }
    
    /**
     * Convert mode string to code.
     */
    private byte encodeModeToCode(String mode) {
        switch (mode.toUpperCase()) {
            case "OFF": return 0;
            case "STANDBY": return 1;
            case "MOTION_TRACKING": return 2;
            case "PERMANENT_TRACKING": return 3;
            case "START_END_TRACKING": return 4;
            case "ACTIVITY_TRACKING": return 5;
            case "SOS_MODE": return 6;
            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }
}