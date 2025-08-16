package com.abeeway.smartbadge.decoders;

import com.abeeway.smartbadge.enums.EventType;
import com.abeeway.smartbadge.models.UplinkData;
import com.abeeway.smartbadge.utils.BitUtils;
import com.abeeway.smartbadge.utils.ByteUtils;

/**
 * Decoder for event messages from Abeeway Smart Badge devices.
 */
public class EventDecoder {
    
    /**
     * Decode event message.
     */
    public void decodeEvent(UplinkData data, byte[] payload) {
        if (payload.length < 2) {
            return; // Not enough data
        }
        
        // First byte contains message type info (already processed)
        // Second byte contains event type
        int eventTypeCode = ByteUtils.toUnsigned(payload[1]);
        data.setEventType(determineEventType(eventTypeCode));
        
        // Additional event data processing
        if (payload.length > 2) {
            String eventData = ByteUtils.bytesToHex(payload).substring(4); // Skip first 2 bytes
            data.setEventData(eventData);
        }
    }
    
    /**
     * Determine event type from event code.
     */
    private EventType determineEventType(int eventCode) {
        switch (eventCode) {
            case 0x01: return EventType.BUTTON_PRESS;
            case 0x02: return EventType.BUTTON_DOUBLE_PRESS;
            case 0x03: return EventType.BUTTON_LONG_PRESS;
            case 0x04: return EventType.SOS_PRESS;
            case 0x05: return EventType.MOTION_START;
            case 0x06: return EventType.MOTION_END;
            case 0x07: return EventType.SHOCK_DETECTION;
            case 0x08: return EventType.TEMPERATURE_ALERT;
            case 0x09: return EventType.GEOFENCE_ENTER;
            case 0x0A: return EventType.GEOFENCE_EXIT;
            case 0x0B: return EventType.PROXIMITY_ENTER;
            case 0x0C: return EventType.PROXIMITY_EXIT;
            case 0x0D: return EventType.BLE_CONNECTION;
            case 0x0E: return EventType.BLE_DISCONNECTION;
            case 0x0F: return EventType.BATTERY_LOW;
            case 0x10: return EventType.BATTERY_CRITICAL;
            default: return EventType.UNKNOWN;
        }
    }
}