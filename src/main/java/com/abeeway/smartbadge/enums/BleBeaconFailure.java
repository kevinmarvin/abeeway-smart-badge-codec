package com.abeeway.smartbadge.enums;

/**
 * Enumeration of BLE beacon failure causes for Abeeway Smart Badge devices.
 */
public enum BleBeaconFailure {
    BLE_NOT_RESPONDING,
    INTERNAL_ERROR,
    SHARED_ANTENNA_NOT_AVAILABLE,
    SCAN_ALREADY_ONGOING,
    NO_BEACON_DETECTED,
    UNKNOWN
}