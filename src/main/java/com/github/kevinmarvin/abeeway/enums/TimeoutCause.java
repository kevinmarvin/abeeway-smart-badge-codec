package com.github.kevinmarvin.abeeway.enums;

/**
 * Enumeration of timeout causes for Abeeway Smart Badge position timeouts.
 */
public enum TimeoutCause {
    USER_TIMEOUT,
    DOP_TIMEOUT,
    EPHEMERIS_TOO_OLD,
    NO_EPHEMERIS,
    ALMANAC_TOO_OLD,
    NO_ALMANAC,
    UNKNOWN
}