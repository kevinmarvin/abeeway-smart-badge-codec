package com.abeeway.smartbadge.utils;

import java.util.Arrays;

/**
 * Utility class for byte array manipulation operations used in Abeeway Smart Badge message processing.
 */
public class ByteUtils {
    
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    
    /**
     * Convert a byte array to a hexadecimal string.
     * 
     * @param bytes The byte array to convert
     * @return The hexadecimal string representation
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "";
        
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            result.append(HEX_CHARS[(b & 0xF0) >>> 4]);
            result.append(HEX_CHARS[b & 0x0F]);
        }
        return result.toString();
    }
    
    /**
     * Convert a hexadecimal string to a byte array.
     * 
     * @param hexString The hex string to convert
     * @return The byte array
     */
    public static byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string");
        }
        
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < result.length; i++) {
            int high = Character.digit(hexString.charAt(i * 2), 16);
            int low = Character.digit(hexString.charAt(i * 2 + 1), 16);
            result[i] = (byte) ((high << 4) + low);
        }
        return result;
    }
    
    /**
     * Convert bytes to an integer (big-endian).
     * 
     * @param bytes  The byte array
     * @param offset The starting offset
     * @param length The number of bytes to convert (max 4)
     * @return The integer value
     */
    public static int bytesToInt(byte[] bytes, int offset, int length) {
        if (length > 4 || offset + length > bytes.length) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        
        int result = 0;
        for (int i = 0; i < length; i++) {
            result = (result << 8) | (bytes[offset + i] & 0xFF);
        }
        return result;
    }
    
    /**
     * Convert bytes to a long (big-endian).
     * 
     * @param bytes  The byte array
     * @param offset The starting offset
     * @param length The number of bytes to convert (max 8)
     * @return The long value
     */
    public static long bytesToLong(byte[] bytes, int offset, int length) {
        if (length > 8 || offset + length > bytes.length) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        
        long result = 0;
        for (int i = 0; i < length; i++) {
            result = (result << 8) | (bytes[offset + i] & 0xFF);
        }
        return result;
    }
    
    /**
     * Convert bytes to an integer (little-endian).
     */
    public static int bytesToIntLE(byte[] bytes, int offset, int length) {
        if (length > 4 || offset + length > bytes.length) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        
        int result = 0;
        for (int i = length - 1; i >= 0; i--) {
            result = (result << 8) | (bytes[offset + i] & 0xFF);
        }
        return result;
    }
    
    /**
     * Convert a signed short value from an unsigned representation.
     * 
     * @param value The unsigned value
     * @return The signed short value
     */
    public static short signedShort(int value) {
        return (short) (value > 32767 ? value - 65536 : value);
    }
    
    /**
     * Convert a signed int value from an unsigned representation.
     */
    public static int signedInt(long value) {
        return (int) (value > Integer.MAX_VALUE ? value - (1L << 32) : value);
    }
    
    /**
     * Extract a subset of bytes from an array.
     */
    public static byte[] extractBytes(byte[] source, int offset, int length) {
        if (offset + length > source.length) {
            throw new IllegalArgumentException("Invalid range");
        }
        return Arrays.copyOfRange(source, offset, offset + length);
    }
    
    /**
     * Concatenate multiple byte arrays.
     */
    public static byte[] concatenate(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
    
    /**
     * Convert an integer to bytes (big-endian).
     */
    public static byte[] intToBytes(int value, int numBytes) {
        byte[] result = new byte[numBytes];
        for (int i = numBytes - 1; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>>= 8;
        }
        return result;
    }
    
    /**
     * Reverse the order of bytes in an array.
     */
    public static byte[] reverse(byte[] bytes) {
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[bytes.length - 1 - i];
        }
        return result;
    }
    
    /**
     * XOR two byte arrays of the same length.
     */
    public static byte[] xor(byte[] a, byte[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Arrays must be the same length");
        }
        
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }
    
    /**
     * Check if two byte arrays are equal.
     */
    public static boolean equals(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
    
    /**
     * Convert a byte to an unsigned integer.
     */
    public static int toUnsigned(byte b) {
        return b & 0xFF;
    }
}