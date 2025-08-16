package com.abeeway.smartbadge.enums;

/**
 * Enumeration of optional commands for Abeeway Smart Badge downlink messages.
 */
public enum OptionalCommand {
    REQUEST_CONFIG,
    SET_MODE,
    REQUEST_POSITION,
    START_SOS,
    STOP_SOS,
    SET_PARAM,
    GET_PARAM,
    RESET_CONFIG,
    CALIBRATE_ACCELEROMETER,
    LED_CONTROL,
    BUZZER_CONTROL,
    UNKNOWN
}