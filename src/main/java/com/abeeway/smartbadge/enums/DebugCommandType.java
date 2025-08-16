package com.abeeway.smartbadge.enums;

/**
 * Enumeration of debug command types for Abeeway Smart Badge devices.
 */
public enum DebugCommandType {
    GET_DEVICE_INFO,
    GET_GPS_STATUS,
    GET_WIFI_STATUS,
    GET_BLE_STATUS,
    GET_BATTERY_STATUS,
    GET_TEMPERATURE,
    GET_ACCELEROMETER,
    RESET_DEVICE,
    FACTORY_RESET,
    GET_LOGS,
    CLEAR_LOGS,
    UNKNOWN
}