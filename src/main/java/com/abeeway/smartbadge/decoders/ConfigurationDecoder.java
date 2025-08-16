package com.abeeway.smartbadge.decoders;

import com.abeeway.smartbadge.models.UplinkData;
import com.abeeway.smartbadge.utils.BitUtils;
import com.abeeway.smartbadge.utils.ByteUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Decoder for configuration messages from Abeeway Smart Badge devices.
 * Handles parameter decoding and configuration data processing.
 */
public class ConfigurationDecoder {
    
    // Parameter definitions would typically be loaded from parameters.json
    private final Map<Integer, ParameterDefinition> parametersById = new HashMap<>();
    
    /**
     * Decode configuration message.
     */
    public void decodeConfiguration(UplinkData data, byte[] payload) {
        Map<String, Object> configuration = new HashMap<>();
        
        int offset = 1; // Skip message type byte
        
        while (offset < payload.length) {
            // Read parameter ID (typically 1 byte)
            if (offset >= payload.length) break;
            
            int paramId = ByteUtils.toUnsigned(payload[offset]);
            offset++;
            
            // Get parameter definition
            ParameterDefinition paramDef = parametersById.get(paramId);
            if (paramDef == null) {
                // Skip unknown parameter - need to determine length somehow
                break;
            }
            
            // Decode parameter value based on its type
            Object value = decodeParameter(payload, offset, paramDef);
            if (value != null) {
                configuration.put(paramDef.getName(), value);
            }
            
            offset += paramDef.getSize();
        }
        
        data.setDeviceConfiguration(configuration);
    }
    
    /**
     * Decode a single parameter based on its definition.
     */
    private Object decodeParameter(byte[] payload, int offset, ParameterDefinition paramDef) {
        if (offset + paramDef.getSize() > payload.length) {
            return null;
        }
        
        switch (paramDef.getType()) {
            case NUMBER:
                return decodeNumberParameter(payload, offset, paramDef);
            case STRING:
                return decodeStringParameter(payload, offset, paramDef);
            case BITMAP:
                return decodeBitmapParameter(payload, offset, paramDef);
            default:
                return null;
        }
    }
    
    /**
     * Decode a number parameter.
     */
    private Object decodeNumberParameter(byte[] payload, int offset, ParameterDefinition paramDef) {
        int size = paramDef.getSize();
        long rawValue;
        
        if (size == 1) {
            rawValue = ByteUtils.toUnsigned(payload[offset]);
        } else if (size == 2) {
            rawValue = ByteUtils.bytesToInt(payload, offset, 2);
        } else if (size <= 4) {
            rawValue = ByteUtils.bytesToLong(payload, offset, size);
        } else {
            return null; // Unsupported size
        }
        
        // Apply scaling if defined
        if (paramDef.getMultiplier() != null && paramDef.getMultiplier() != 1.0) {
            return rawValue * paramDef.getMultiplier();
        }
        
        return (int) rawValue;
    }
    
    /**
     * Decode a string parameter (enum mapping).
     */
    private Object decodeStringParameter(byte[] payload, int offset, ParameterDefinition paramDef) {
        int size = paramDef.getSize();
        long rawValue;
        
        if (size == 1) {
            rawValue = ByteUtils.toUnsigned(payload[offset]);
        } else if (size <= 4) {
            rawValue = ByteUtils.bytesToLong(payload, offset, size);
        } else {
            return null;
        }
        
        // Map raw value to string using parameter definition
        return paramDef.mapValueToString((int) rawValue);
    }
    
    /**
     * Decode a bitmap parameter.
     */
    private Object decodeBitmapParameter(byte[] payload, int offset, ParameterDefinition paramDef) {
        int size = paramDef.getSize();
        Map<String, Object> bitmap = new HashMap<>();
        
        // Read raw bytes
        for (int i = 0; i < size && offset + i < payload.length; i++) {
            int byteValue = ByteUtils.toUnsigned(payload[offset + i]);
            
            // Decode individual bits according to parameter definition
            for (int bit = 0; bit < 8; bit++) {
                boolean bitValue = BitUtils.getBit(byteValue, bit);
                String bitName = paramDef.getBitName(i * 8 + bit);
                if (bitName != null) {
                    bitmap.put(bitName, bitValue);
                }
            }
        }
        
        return bitmap;
    }
    
    /**
     * Initialize parameter definitions.
     * In a full implementation, this would load from parameters.json.
     */
    public void initializeParameters() {
        // Example parameter definitions
        parametersById.put(0x01, new ParameterDefinition(
            0x01, "ul_period", ParameterType.NUMBER, 2, 1.0, null
        ));
        
        parametersById.put(0x02, new ParameterDefinition(
            0x02, "lora_period", ParameterType.NUMBER, 2, 1.0, null
        ));
        
        // Add more parameter definitions as needed
    }
    
    /**
     * Parameter type enumeration.
     */
    public enum ParameterType {
        NUMBER, STRING, BITMAP
    }
    
    /**
     * Parameter definition class.
     * In a full implementation, this would be loaded from JSON.
     */
    public static class ParameterDefinition {
        private final int id;
        private final String name;
        private final ParameterType type;
        private final int size;
        private final Double multiplier;
        private final Map<Integer, String> valueMapping;
        private final Map<Integer, String> bitNames;
        
        public ParameterDefinition(int id, String name, ParameterType type, int size, 
                                 Double multiplier, Map<Integer, String> valueMapping) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.size = size;
            this.multiplier = multiplier;
            this.valueMapping = valueMapping != null ? valueMapping : new HashMap<>();
            this.bitNames = new HashMap<>();
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public ParameterType getType() { return type; }
        public int getSize() { return size; }
        public Double getMultiplier() { return multiplier; }
        
        public String mapValueToString(int value) {
            return valueMapping.getOrDefault(value, String.valueOf(value));
        }
        
        public String getBitName(int bitPosition) {
            return bitNames.get(bitPosition);
        }
        
        public void addBitName(int bitPosition, String name) {
            bitNames.put(bitPosition, name);
        }
    }
}