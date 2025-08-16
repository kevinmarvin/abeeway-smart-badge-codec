package com.abeeway.smartbadge.models;

/**
 * Container for encoded downlink command data for an Abeeway Smart Badge device.
 */
public class EncodedDownlink {
    private byte[] bytes;
    private int fPort;

    public EncodedDownlink() {
    }

    public EncodedDownlink(byte[] bytes, int fPort) {
        this.bytes = bytes;
        this.fPort = fPort;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getfPort() {
        return fPort;
    }

    public void setfPort(int fPort) {
        this.fPort = fPort;
    }
}