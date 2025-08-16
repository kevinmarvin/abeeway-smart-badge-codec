package com.github.kevinmarvin.abeeway.encoders;

import com.github.kevinmarvin.abeeway.exceptions.EncodingException;
import com.github.kevinmarvin.abeeway.models.EncodedDownlink;
import com.github.kevinmarvin.abeeway.parameters.AbeewayParams;
import com.github.kevinmarvin.abeeway.parameters.ParameterRegistry;
import com.github.kevinmarvin.abeeway.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Encoder for Abeeway Smart Badge parameters using high-level parameter objects.
 */
public class ParameterEncoder {
    
    private final ParameterRegistry registry = ParameterRegistry.getInstance();
    
    /**
     * Encode a set of parameters for downlink transmission.
     */
    public EncodedDownlink encodeParameters(AbeewayParams params, int fPort) throws EncodingException {
        List<Byte> encodedBytes = new ArrayList<>();
        
        // Add SET_PARAM command header
        encodedBytes.add((byte) 0x09); // SET_PARAM command
        
        // Encode each parameter
        Map<String, Object> parameters = params.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String paramName = entry.getKey();
            Object value = entry.getValue();
            
            // Handle custom parameters (by ID)
            if (paramName.startsWith("custom_")) {
                int paramId = Integer.parseInt(paramName.substring(7));
                encodeParameter(encodedBytes, paramId, value);
            } else {
                // Look up parameter ID from registry
                Integer paramId = registry.getParameterId(paramName);
                if (paramId == null) {
                    throw new EncodingException("Unknown parameter: " + paramName);
                }
                encodeParameter(encodedBytes, paramId, value);
            }
        }
        
        // Convert to byte array
        byte[] result = new byte[encodedBytes.size()];
        for (int i = 0; i < encodedBytes.size(); i++) {
            result[i] = encodedBytes.get(i);
        }
        
        return new EncodedDownlink(result, fPort);
    }
    
    /**
     * Encode a single configuration command (mode, request, etc.).
     */
    public EncodedDownlink encodeConfigurationCommand(ConfigurationCommand command, int fPort) 
            throws EncodingException {
        
        byte[] encodedBytes;
        
        switch (command.getType()) {
            case REQUEST_CONFIG:
                encodedBytes = new byte[]{0x01}; // REQUEST_CONFIG command
                break;
                
            case SET_MODE:
                if (command.getValue() == null) {
                    throw new EncodingException("SET_MODE command requires a mode value");
                }
                byte modeCode = encodeModeToCode(command.getValue().toString());
                encodedBytes = new byte[]{0x02, modeCode}; // SET_MODE command
                break;
                
            case REQUEST_POSITION:
                encodedBytes = new byte[]{0x04}; // REQUEST_POSITION command
                break;
                
            case RESET_CONFIG:
                encodedBytes = new byte[]{0x05}; // RESET_CONFIG command
                break;
                
            default:
                throw new EncodingException("Unknown configuration command: " + command.getType());
        }
        
        return new EncodedDownlink(encodedBytes, fPort);
    }
    
    /**
     * Create a configuration request command.
     */
    public static ConfigurationCommand requestConfiguration() {
        return new ConfigurationCommand(ConfigurationCommand.Type.REQUEST_CONFIG, null);
    }
    
    /**
     * Create a set mode command.
     */
    public static ConfigurationCommand setMode(AbeewayParams.OperatingMode mode) {
        return new ConfigurationCommand(ConfigurationCommand.Type.SET_MODE, mode.name());
    }
    
    /**
     * Create a position request command.
     */
    public static ConfigurationCommand requestPosition() {
        return new ConfigurationCommand(ConfigurationCommand.Type.REQUEST_POSITION, null);
    }
    
    /**
     * Create a reset configuration command.
     */
    public static ConfigurationCommand resetConfiguration() {
        return new ConfigurationCommand(ConfigurationCommand.Type.RESET_CONFIG, null);
    }
    
    /**
     * Encode a single parameter.
     */
    private void encodeParameter(List<Byte> bytes, int parameterId, Object value) throws EncodingException {
        // Add parameter ID
        bytes.add((byte) parameterId);
        
        // Encode value based on type
        if (value instanceof Number) {
            int intValue = ((Number) value).intValue();
            
            // Determine encoding size based on parameter ID (simplified logic)
            if (intValue <= 255) {
                // Single byte value
                bytes.add((byte) intValue);
            } else if (intValue <= 65535) {
                // Two byte value (little endian)
                bytes.add((byte) (intValue & 0xFF));
                bytes.add((byte) ((intValue >> 8) & 0xFF));
            } else {
                // Four byte value (little endian)
                bytes.add((byte) (intValue & 0xFF));
                bytes.add((byte) ((intValue >> 8) & 0xFF));
                bytes.add((byte) ((intValue >> 16) & 0xFF));
                bytes.add((byte) ((intValue >> 24) & 0xFF));
            }
        } else {
            throw new EncodingException("Unsupported parameter value type: " + value.getClass());
        }
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
    
    /**
     * Configuration command class.
     */
    public static class ConfigurationCommand {
        public enum Type {
            REQUEST_CONFIG,
            SET_MODE,
            REQUEST_POSITION,
            RESET_CONFIG
        }
        
        private final Type type;
        private final Object value;
        
        public ConfigurationCommand(Type type, Object value) {
            this.type = type;
            this.value = value;
        }
        
        public Type getType() { return type; }
        public Object getValue() { return value; }
    }
}