package com.abeeway.smartbadge.enums;

/**
 * Enumeration of shutdown causes for Abeeway Smart Badge devices.
 */
public enum ShutdownCause {
    BATTERY_EXHAUSTED,
    USER_SHUTDOWN,
    BLE_DISCONNECT,
    WATCHDOG_RESET,
    FIRMWARE_UPDATE,
    TEMPERATURE_PROTECTION,
    HARDWARE_FAILURE,
    UNKNOWN
}