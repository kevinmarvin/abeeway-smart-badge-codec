package com.abeeway.smartbadge.decoders;

import com.abeeway.smartbadge.enums.*;
import com.abeeway.smartbadge.models.*;
import com.abeeway.smartbadge.utils.BitUtils;
import com.abeeway.smartbadge.utils.ByteUtils;
import com.abeeway.smartbadge.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Decoder for position-related messages from Abeeway Smart Badge devices.
 * Handles GPS, WiFi, and BLE position data decoding.
 */
public class PositionDecoder {
    
    /**
     * Decode a position message.
     */
    public void decodePositionMessage(UplinkData data, byte[] payload) {
        data.setRawPositionType(determineRawPositionType(payload));
        
        switch (data.getRawPositionType()) {
            case GPS:
                decodeGpsPosition(data, payload, MessageType.POSITION_MESSAGE);
                break;
            case GPS_TIMEOUT:
                decodeGpsTimeout(data, payload, MessageType.POSITION_MESSAGE);
                break;
            case WIFI_BSSIDS_WITH_NO_CYPHER:
                decodeWifiPosition(data, payload, MessageType.POSITION_MESSAGE, false);
                break;
            case WIFI_BSSIDS_WITH_CYPHER:
                decodeWifiPosition(data, payload, MessageType.POSITION_MESSAGE, true);
                break;
            case BLE_BEACON_SCAN:
                decodeBlePosition(data, payload, MessageType.POSITION_MESSAGE);
                break;
            case BLE_BEACON_FAILURE:
                decodeBleFailure(data, payload);
                break;
            case WIFI_FAILURE:
                decodeWifiFailure(data, payload);
                break;
            case GPS_WITH_EHPE:
                decodeGpsWithEhpe(data, payload, MessageType.POSITION_MESSAGE);
                break;
            default:
                // Handle other position types as needed
                break;
        }
    }
    
    /**
     * Decode an extended position message.
     */
    public void decodeExtendedPositionMessage(UplinkData data, byte[] payload) {
        data.setRawPositionType(determineRawPositionType(payload));
        
        switch (data.getRawPositionType()) {
            case GPS:
                decodeGpsPosition(data, payload, MessageType.EXTENDED_POSITION_MESSAGE);
                break;
            case GPS_TIMEOUT:
                decodeGpsTimeout(data, payload, MessageType.EXTENDED_POSITION_MESSAGE);
                break;
            case WIFI_BSSIDS_WITH_NO_CYPHER:
                decodeWifiPosition(data, payload, MessageType.EXTENDED_POSITION_MESSAGE, false);
                break;
            case WIFI_BSSIDS_WITH_CYPHER:
                decodeWifiPosition(data, payload, MessageType.EXTENDED_POSITION_MESSAGE, true);
                break;
            case BLE_BEACON_SCAN:
                decodeBlePosition(data, payload, MessageType.EXTENDED_POSITION_MESSAGE);
                break;
            default:
                // Handle other position types
                break;
        }
    }
    
    /**
     * Determine the raw position type from the payload.
     */
    private RawPositionType determineRawPositionType(byte[] payload) {
        if (payload.length < 1) {
            return RawPositionType.UNKNOWN;
        }
        
        int firstByte = ByteUtils.toUnsigned(payload[0]);
        int positionType = BitUtils.extractBits(firstByte, 4, 4);
        
        switch (positionType) {
            case 0: return RawPositionType.GPS;
            case 1: return RawPositionType.GPS_TIMEOUT;
            case 2: return RawPositionType.WIFI_BSSIDS_WITH_NO_CYPHER;
            case 3: return RawPositionType.WIFI_BSSIDS_WITH_CYPHER;
            case 4: return RawPositionType.BLE_BEACON_SCAN;
            case 5: return RawPositionType.BLE_BEACON_FAILURE;
            case 6: return RawPositionType.WIFI_FAILURE;
            case 7: return RawPositionType.WIFI_TIMEOUT;
            case 8: return RawPositionType.GPS_WIFI_BLE_SCAN;
            case 9: return RawPositionType.GPS_WIFI_FAILURE;
            case 10: return RawPositionType.GPS_BLE_FAILURE;
            case 11: return RawPositionType.WIFI_BLE_FAILURE;
            case 12: return RawPositionType.GPS_WIFI_BLE_FAILURE;
            case 13: return RawPositionType.GPS_WITH_EHPE;
            default: return RawPositionType.UNKNOWN;
        }
    }
    
    /**
     * Decode GPS position data.
     */
    private void decodeGpsPosition(UplinkData data, byte[] payload, MessageType msgType) {
        data.setAge(determineAge(payload));
        data.setGpsLatitude(determineLatitude(payload, msgType));
        data.setGpsLongitude(determineLongitude(payload, msgType));
        data.setHorizontalAccuracy(determineHorizontalAccuracy(payload, msgType));
    }
    
    /**
     * Decode GPS position with enhanced horizontal positioning error (EHPE).
     */
    private void decodeGpsWithEhpe(UplinkData data, byte[] payload, MessageType msgType) {
        decodeGpsPosition(data, payload, msgType);
        // Additional EHPE decoding would go here
    }
    
    /**
     * Decode GPS timeout information.
     */
    private void decodeGpsTimeout(UplinkData data, byte[] payload, MessageType msgType) {
        if (payload.length >= 2) {
            int timeoutInfo = ByteUtils.toUnsigned(payload[1]);
            data.setWifiTimeoutCause(determineTimeoutCause(timeoutInfo));
        }
    }
    
    /**
     * Decode WiFi position data.
     */
    private void decodeWifiPosition(UplinkData data, byte[] payload, MessageType msgType, boolean withCypher) {
        List<WiFiAccessPoint> accessPoints = new ArrayList<>();
        int startIndex = 1; // Skip the first byte (message type info)
        
        int apSize = withCypher ? 7 : 6; // MAC + RSSI + optional cipher info
        int maxAPs = (payload.length - startIndex) / apSize;
        
        for (int i = 0; i < maxAPs; i++) {
            int offset = startIndex + (i * apSize);
            if (offset + apSize <= payload.length) {
                WiFiAccessPoint ap = decodeWifiAccessPoint(payload, offset, withCypher);
                if (ap != null) {
                    accessPoints.add(ap);
                }
            }
        }
        
        data.setWifiAccessPoints(accessPoints);
    }
    
    /**
     * Decode a single WiFi access point.
     */
    private WiFiAccessPoint decodeWifiAccessPoint(byte[] payload, int offset, boolean withCypher) {
        if (offset + 6 > payload.length) return null;
        
        // Extract MAC address (6 bytes)
        byte[] macBytes = Arrays.copyOfRange(payload, offset, offset + 6);
        String macAddress = ByteUtils.bytesToHex(macBytes);
        
        // Extract RSSI (1 byte)
        int rssi = payload[offset + 6] & 0xFF;
        if (rssi > 127) rssi = rssi - 256; // Convert to signed
        
        WiFiAccessPoint ap = new WiFiAccessPoint(formatMacAddress(macAddress), rssi);
        
        if (withCypher && offset + 7 <= payload.length) {
            int cypherInfo = ByteUtils.toUnsigned(payload[offset + 6]);
            ap.setEncrypted((cypherInfo & 0x01) != 0);
        }
        
        return ap;
    }
    
    /**
     * Decode BLE position data.
     */
    private void decodeBlePosition(UplinkData data, byte[] payload, MessageType msgType) {
        List<BleBeacon> beacons = new ArrayList<>();
        int startIndex = 1;
        
        int beaconSize = 7; // MAC (6) + RSSI (1)
        int maxBeacons = (payload.length - startIndex) / beaconSize;
        
        for (int i = 0; i < maxBeacons; i++) {
            int offset = startIndex + (i * beaconSize);
            if (offset + beaconSize <= payload.length) {
                BleBeacon beacon = decodeBleBeacon(payload, offset);
                if (beacon != null) {
                    beacons.add(beacon);
                }
            }
        }
        
        data.setBleBeacons(beacons);
    }
    
    /**
     * Decode a single BLE beacon.
     */
    private BleBeacon decodeBleBeacon(byte[] payload, int offset) {
        if (offset + 7 > payload.length) return null;
        
        // Extract MAC address (6 bytes)
        byte[] macBytes = Arrays.copyOfRange(payload, offset, offset + 6);
        String macAddress = ByteUtils.bytesToHex(macBytes);
        
        // Extract RSSI (1 byte)
        int rssi = payload[offset + 6] & 0xFF;
        if (rssi > 127) rssi = rssi - 256;
        
        return new BleBeacon(formatMacAddress(macAddress), rssi);
    }
    
    /**
     * Decode BLE beacon failure.
     */
    private void decodeBleFailure(UplinkData data, byte[] payload) {
        if (payload.length >= 2) {
            int failureCode = ByteUtils.toUnsigned(payload[1]);
            data.setBleBeaconFailure(determineBleFailure(failureCode));
        }
    }
    
    /**
     * Decode WiFi failure.
     */
    private void decodeWifiFailure(UplinkData data, byte[] payload) {
        if (payload.length >= 2) {
            int failureCode = ByteUtils.toUnsigned(payload[1]);
            data.setWifiFailure(failureCode);
        }
    }
    
    /**
     * Determine the age from payload.
     */
    private Integer determineAge(byte[] payload) {
        if (payload.length < 2) return null;
        int firstByte = ByteUtils.toUnsigned(payload[0]);
        return BitUtils.extractBits(firstByte, 0, 4);
    }
    
    /**
     * Determine latitude from GPS coordinates.
     */
    private Double determineLatitude(byte[] payload, MessageType messageType) {
        int startIdx, endIdx;
        String padding = "";
        
        switch (messageType) {
            case EXTENDED_POSITION_MESSAGE:
                startIdx = 8; endIdx = 12; padding = "";
                break;
            case POSITION_MESSAGE:
                startIdx = 6; endIdx = 9; padding = "00";
                break;
            default:
                return null;
        }
        
        if (payload.length < endIdx) return null;
        
        byte[] coordBytes = Arrays.copyOfRange(payload, startIdx, endIdx);
        String hexStr = ByteUtils.bytesToHex(coordBytes) + padding;
        long rawValue = Long.parseUnsignedLong(hexStr, 16);
        
        // Handle signed conversion for 32-bit value
        if (rawValue > 2147483647L) {
            rawValue -= 4294967296L;
        }
        
        double latitude = rawValue / Math.pow(10, 7);
        return ValidationUtils.isValidLatitude(latitude) ? latitude : null;
    }
    
    /**
     * Determine longitude from GPS coordinates.
     */
    private Double determineLongitude(byte[] payload, MessageType messageType) {
        int startIdx, endIdx;
        String padding = "";
        
        switch (messageType) {
            case EXTENDED_POSITION_MESSAGE:
                startIdx = 4; endIdx = 8; padding = "";
                break;
            case POSITION_MESSAGE:
                startIdx = 3; endIdx = 6; padding = "00";
                break;
            default:
                return null;
        }
        
        if (payload.length < endIdx) return null;
        
        byte[] coordBytes = Arrays.copyOfRange(payload, startIdx, endIdx);
        String hexStr = ByteUtils.bytesToHex(coordBytes) + padding;
        long rawValue = Long.parseUnsignedLong(hexStr, 16);
        
        // Handle signed conversion for 32-bit value
        if (rawValue > 2147483647L) {
            rawValue -= 4294967296L;
        }
        
        double longitude = rawValue / Math.pow(10, 7);
        return ValidationUtils.isValidLongitude(longitude) ? longitude : null;
    }
    
    /**
     * Determine horizontal accuracy.
     */
    private Object determineHorizontalAccuracy(byte[] payload, MessageType messageType) {
        // Implementation would depend on the specific format in the payload
        // This is a simplified version
        return null;
    }
    
    /**
     * Determine timeout cause from failure code.
     */
    private TimeoutCause determineTimeoutCause(int failureCode) {
        switch (failureCode & 0x07) {
            case 0: return TimeoutCause.USER_TIMEOUT;
            case 1: return TimeoutCause.DOP_TIMEOUT;
            case 2: return TimeoutCause.EPHEMERIS_TOO_OLD;
            case 3: return TimeoutCause.NO_EPHEMERIS;
            case 4: return TimeoutCause.ALMANAC_TOO_OLD;
            case 5: return TimeoutCause.NO_ALMANAC;
            default: return TimeoutCause.UNKNOWN;
        }
    }
    
    /**
     * Determine BLE failure from failure code.
     */
    private BleBeaconFailure determineBleFailure(int failureCode) {
        switch (failureCode & 0x07) {
            case 0: return BleBeaconFailure.BLE_NOT_RESPONDING;
            case 1: return BleBeaconFailure.INTERNAL_ERROR;
            case 2: return BleBeaconFailure.SHARED_ANTENNA_NOT_AVAILABLE;
            case 3: return BleBeaconFailure.SCAN_ALREADY_ONGOING;
            case 4: return BleBeaconFailure.NO_BEACON_DETECTED;
            default: return BleBeaconFailure.UNKNOWN;
        }
    }
    
    /**
     * Format MAC address with colons.
     */
    private String formatMacAddress(String macAddress) {
        if (macAddress.length() != 12) return macAddress;
        
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < macAddress.length(); i += 2) {
            if (i > 0) formatted.append(":");
            formatted.append(macAddress.substring(i, i + 2));
        }
        return formatted.toString();
    }
}