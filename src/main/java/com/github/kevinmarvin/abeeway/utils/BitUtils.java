package com.github.kevinmarvin.abeeway.utils;

/**
 * Utility class for bit manipulation operations used in Abeeway Smart Badge message decoding.
 */
public class BitUtils {
    
    /**
     * Extract a specified number of bits from a value starting at a given position.
     * 
     * @param value  The source value
     * @param start  The starting bit position (0-based from right)
     * @param length The number of bits to extract
     * @return The extracted bits as an integer
     */
    public static int extractBits(int value, int start, int length) {
        int mask = (1 << length) - 1;
        return (value >> start) & mask;
    }
    
    /**
     * Extract bits from a long value.
     */
    public static long extractBits(long value, int start, int length) {
        long mask = (1L << length) - 1;
        return (value >> start) & mask;
    }
    
    /**
     * Get the value of a specific bit.
     * 
     * @param value    The source value
     * @param position The bit position (0-based from right)
     * @return true if the bit is set, false otherwise
     */
    public static boolean getBit(int value, int position) {
        return (value & (1 << position)) != 0;
    }
    
    /**
     * Get the value of a specific bit from a long.
     */
    public static boolean getBit(long value, int position) {
        return (value & (1L << position)) != 0;
    }
    
    /**
     * Scale a raw integer value to a double within a specified range.
     * This is commonly used for sensor data scaling in the Abeeway protocol.
     * 
     * @param rawValue The raw integer value
     * @param min      The minimum scaled value
     * @param max      The maximum scaled value
     * @param bits     The number of bits used for the raw value
     * @param offset   The offset to apply before scaling
     * @return The scaled double value
     */
    public static double scaleValue(int rawValue, double min, double max, int bits, double offset) {
        if (bits <= 0) return min;
        
        double adjustedValue = rawValue - offset / 2.0;
        double scale = (max - min) / ((1 << bits) - 1 - offset);
        return adjustedValue * scale + min;
    }
    
    /**
     * Scale a raw value using a multiplier.
     */
    public static double scaleWithMultiplier(int rawValue, double multiplier, double offset) {
        return (rawValue * multiplier) + offset;
    }
    
    /**
     * Convert an unsigned byte to an integer.
     */
    public static int unsignedByte(byte value) {
        return value & 0xFF;
    }
    
    /**
     * Convert an unsigned short to an integer.
     */
    public static int unsignedShort(short value) {
        return value & 0xFFFF;
    }
    
    /**
     * Convert bytes to an unsigned integer (big-endian).
     */
    public static long bytesToUnsignedInt(byte[] bytes, int offset, int length) {
        long result = 0;
        for (int i = 0; i < length && i < 8; i++) {
            result = (result << 8) | unsignedByte(bytes[offset + i]);
        }
        return result;
    }
    
    /**
     * Convert a nibble (4 bits) to hex character.
     */
    public static char nibbleToHex(int nibble) {
        nibble &= 0xF;
        return (char) (nibble < 10 ? '0' + nibble : 'A' + nibble - 10);
    }
    
    /**
     * Reverse the bit order in a byte.
     */
    public static byte reverseBits(byte b) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 1) | ((b >> i) & 1);
        }
        return (byte) result;
    }
    
    /**
     * Count the number of set bits in an integer.
     */
    public static int countSetBits(int value) {
        return Integer.bitCount(value);
    }
    
    /**
     * Check if a value fits within the specified number of bits.
     */
    public static boolean fitsInBits(long value, int bits) {
        if (bits <= 0) return false;
        if (bits >= 64) return true;
        
        long max = (1L << bits) - 1;
        return value >= 0 && value <= max;
    }
}