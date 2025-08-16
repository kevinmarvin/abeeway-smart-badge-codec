package com.abeeway.smartbadge.enums;

/**
 * Enumeration of all message types supported by the Abeeway Smart Badge.
 */
public enum MessageType {
    POSITION_MESSAGE,
    EXTENDED_POSITION_MESSAGE,
    HEARTBEAT,
    ENERGY_STATUS,
    HEALTH_STATUS,
    SHUTDOWN,
    FRAME_PENDING,
    DEBUG,
    ACTIVITY_STATUS,
    CONFIGURATION,
    SHOCK_DETECTION,
    BLE_MAC,
    EVENT,
    DATA_SCAN_COLLECTION,
    PROXIMITY_DETECTION,
    SMS,
    UNKNOWN
}