package com.github.kevinmarvin.abeeway.utils;

/**
 * Utility class for validation operations used in Abeeway Smart Badge message processing.
 */
public class ValidationUtils {
    
    /**
     * Validate that a value is within a specified range.
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Validate that a value is within a specified range.
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    /**
     * Validate that a latitude is valid (-90 to +90 degrees).
     */
    public static boolean isValidLatitude(double latitude) {
        return isInRange(latitude, -90.0, 90.0);
    }
    
    /**
     * Validate that a longitude is valid (-180 to +180 degrees).
     */
    public static boolean isValidLongitude(double longitude) {
        return isInRange(longitude, -180.0, 180.0);
    }
    
    /**
     * Validate that a MAC address string has the correct format.
     */
    public static boolean isValidMacAddress(String macAddress) {
        if (macAddress == null) return false;
        
        // Remove separators and check if it's 12 hex characters
        String cleaned = macAddress.replaceAll("[:-]", "");
        return cleaned.length() == 12 && cleaned.matches("[0-9A-Fa-f]+");
    }
    
    /**
     * Validate that a hex string is valid.
     */
    public static boolean isValidHexString(String hex) {
        if (hex == null || hex.length() % 2 != 0) return false;
        return hex.matches("[0-9A-Fa-f]*");
    }
    
    /**
     * Validate that an array has the expected length.
     */
    public static boolean hasExpectedLength(byte[] array, int expectedLength) {
        return array != null && array.length == expectedLength;
    }
    
    /**
     * Validate that an array has at least the minimum required length.
     */
    public static boolean hasMinimumLength(byte[] array, int minLength) {
        return array != null && array.length >= minLength;
    }
    
    /**
     * Validate that a string is not null or empty.
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate that a battery level is within valid range (0-100%).
     */
    public static boolean isValidBatteryLevel(int batteryLevel) {
        return isInRange(batteryLevel, 0, 100);
    }
    
    /**
     * Validate that a temperature is within reasonable range for device operation.
     */
    public static boolean isValidTemperature(double temperature) {
        return isInRange(temperature, -40.0, 85.0); // Typical operating range for electronics
    }
    
    /**
     * Validate that an RSSI value is within typical WiFi/BLE range.
     */
    public static boolean isValidRssi(int rssi) {
        return isInRange(rssi, -120, 0); // dBm range
    }
    
    /**
     * Validate that a frame port is within LoRaWAN specification.
     */
    public static boolean isValidFramePort(int fPort) {
        return isInRange(fPort, 1, 223); // LoRaWAN application port range
    }
    
    /**
     * Validate that a timestamp is reasonable (not too far in past or future).
     */
    public static boolean isValidTimestamp(long timestamp) {
        long now = System.currentTimeMillis();
        long oneYearMs = 365L * 24 * 60 * 60 * 1000;
        
        // Allow timestamps within ±1 year of current time
        return timestamp > (now - oneYearMs) && timestamp < (now + oneYearMs);
    }
    
    /**
     * Clamp a value to be within the specified range.
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamp a value to be within the specified range.
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Check if a value is a power of 2.
     */
    public static boolean isPowerOfTwo(int value) {
        return value > 0 && (value & (value - 1)) == 0;
    }
    
    /**
     * Validate that a UUID string has the correct format.
     */
    public static boolean isValidUuid(String uuid) {
        if (uuid == null) return false;
        
        // Standard UUID format: 8-4-4-4-12 hex digits with hyphens
        return uuid.matches("[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}");
    }
}