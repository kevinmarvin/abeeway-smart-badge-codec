package com.github.kevinmarvin.abeeway.enums;

/**
 * Enumeration of raw position types for Abeeway Smart Badge position messages.
 */
public enum RawPositionType {
    GPS,
    GPS_TIMEOUT,
    WIFI_BSSIDS_WITH_NO_CYPHER,
    WIFI_BSSIDS_WITH_CYPHER,
    BLE_BEACON_SCAN,
    BLE_BEACON_FAILURE,
    WIFI_FAILURE,
    WIFI_TIMEOUT,
    GPS_WIFI_BLE_SCAN,
    GPS_WIFI_FAILURE,
    GPS_BLE_FAILURE,
    WIFI_BLE_FAILURE,
    GPS_WIFI_BLE_FAILURE,
    GPS_WITH_EHPE,
    UNKNOWN
}