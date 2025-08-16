package com.github.kevinmarvin.abeeway.enums;

/**
 * Enumeration of event types for Abeeway Smart Badge event messages.
 */
public enum EventType {
    BUTTON_PRESS,
    BUTTON_DOUBLE_PRESS,
    BUTTON_LONG_PRESS,
    SOS_PRESS,
    MOTION_START,
    MOTION_END,
    SHOCK_DETECTION,
    TEMPERATURE_ALERT,
    GEOFENCE_ENTER,
    GEOFENCE_EXIT,
    PROXIMITY_ENTER,
    PROXIMITY_EXIT,
    BLE_CONNECTION,
    BLE_DISCONNECTION,
    BATTERY_LOW,
    BATTERY_CRITICAL,
    UNKNOWN
}