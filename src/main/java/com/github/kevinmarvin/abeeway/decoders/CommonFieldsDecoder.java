package com.github.kevinmarvin.abeeway.decoders;

import com.github.kevinmarvin.abeeway.enums.BatteryStatus;
import com.github.kevinmarvin.abeeway.enums.DynamicMotionState;
import com.github.kevinmarvin.abeeway.models.UplinkData;
import com.github.kevinmarvin.abeeway.utils.BitUtils;
import com.github.kevinmarvin.abeeway.utils.ByteUtils;

/**
 * Decoder for common fields present in most Abeeway Smart Badge messages.
 */
public class CommonFieldsDecoder {
    
    /**
     * Extract common fields from the payload.
     */
    public void extractCommonFields(UplinkData data, byte[] payload, int fPort) {
        if (payload.length < 1) return;
        
        // First byte typically contains common flags
        int firstByte = ByteUtils.toUnsigned(payload[0]);
        
        // Extract acknowledge token (bits 0-3)
        data.setAckToken(BitUtils.extractBits(firstByte, 0, 4));
        
        // Extract tracking mode and other flags from subsequent bytes if available
        if (payload.length >= 2) {
            extractTrackingInfo(data, payload);
        }
        
        if (payload.length >= 3) {
            extractBatteryInfo(data, payload);
        }
        
        if (payload.length >= 4) {
            extractMotionInfo(data, payload);
        }
    }
    
    /**
     * Extract tracking mode information.
     */
    private void extractTrackingInfo(UplinkData data, byte[] payload) {
        int trackingByte = ByteUtils.toUnsigned(payload[1]);
        
        // Extract on-demand flag
        data.setOnDemand(BitUtils.getBit(trackingByte, 7));
        
        // Extract periodic position flag
        data.setPeriodicPosition(BitUtils.getBit(trackingByte, 6));
        
        // Extract SOS flag
        data.setSosFlag(BitUtils.extractBits(trackingByte, 0, 2));
        
        // Extract tracking mode
        int modeCode = BitUtils.extractBits(trackingByte, 2, 4);
        data.setTrackingMode(determineTrackingMode(modeCode));
    }
    
    /**
     * Extract battery information.
     */
    private void extractBatteryInfo(UplinkData data, byte[] payload) {
        if (payload.length >= 3) {
            int batteryByte = ByteUtils.toUnsigned(payload[2]);
            
            // Battery level (0-100%)
            int batteryLevel = BitUtils.extractBits(batteryByte, 0, 7);
            data.setBatteryLevel(batteryLevel);
            
            // Battery status from MSB
            boolean critical = BitUtils.getBit(batteryByte, 7);
            data.setBatteryStatus(critical ? BatteryStatus.CRITICAL : BatteryStatus.OPERATING);
        }
        
        // Battery voltage might be in additional bytes
        if (payload.length >= 5) {
            int voltageByte = ByteUtils.bytesToInt(payload, 3, 2);
            double voltage = voltageByte * 0.01; // Typical scaling
            data.setBatteryVoltage(voltage);
        }
    }
    
    /**
     * Extract motion and activity information.
     */
    private void extractMotionInfo(UplinkData data, byte[] payload) {
        if (payload.length >= 4) {
            int motionByte = ByteUtils.toUnsigned(payload[3]);
            
            // Dynamic motion state
            int motionState = BitUtils.extractBits(motionByte, 0, 2);
            data.setDynamicMotionState(determineDynamicMotionState(motionState));
            
            // App state
            int appState = BitUtils.extractBits(motionByte, 2, 6);
            data.setAppState(appState);
        }
    }
    
    /**
     * Extract temperature information if present.
     */
    public void extractTemperature(UplinkData data, byte[] payload, int offset) {
        if (payload.length > offset) {
            int tempRaw = ByteUtils.toUnsigned(payload[offset]);
            // Typical temperature scaling: (raw - 200) / 8
            double temperature = (tempRaw - 200) / 8.0;
            data.setTemperatureMeasure(temperature);
        }
    }
    
    /**
     * Determine tracking mode from code.
     */
    private String determineTrackingMode(int modeCode) {
        switch (modeCode) {
            case 0: return "OFF";
            case 1: return "STANDBY";
            case 2: return "MOTION_TRACKING";
            case 3: return "PERMANENT_TRACKING";
            case 4: return "START_END_TRACKING";
            case 5: return "ACTIVITY_TRACKING";
            case 6: return "SOS_MODE";
            default: return "UNKNOWN";
        }
    }
    
    /**
     * Determine dynamic motion state from code.
     */
    private DynamicMotionState determineDynamicMotionState(int stateCode) {
        switch (stateCode) {
            case 0: return DynamicMotionState.STATIC;
            case 1: return DynamicMotionState.START_MOVING;
            case 2: return DynamicMotionState.MOVING;
            case 3: return DynamicMotionState.START_STATIC;
            default: return DynamicMotionState.UNKNOWN;
        }
    }
}