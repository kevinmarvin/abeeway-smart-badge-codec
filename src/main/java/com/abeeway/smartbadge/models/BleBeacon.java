package com.abeeway.smartbadge.models;

/**
 * Data class representing a BLE beacon detected during position scanning.
 */
public class BleBeacon {
    private String macAddress;
    private Integer rssi;
    private String uuid;
    private Integer major;
    private Integer minor;
    private Integer txPower;
    private String beaconType;
    
    public BleBeacon() {
    }
    
    public BleBeacon(String macAddress, Integer rssi) {
        this.macAddress = macAddress;
        this.rssi = rssi;
    }
    
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    
    public Integer getRssi() { return rssi; }
    public void setRssi(Integer rssi) { this.rssi = rssi; }
    
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    
    public Integer getMajor() { return major; }
    public void setMajor(Integer major) { this.major = major; }
    
    public Integer getMinor() { return minor; }
    public void setMinor(Integer minor) { this.minor = minor; }
    
    public Integer getTxPower() { return txPower; }
    public void setTxPower(Integer txPower) { this.txPower = txPower; }
    
    public String getBeaconType() { return beaconType; }
    public void setBeaconType(String beaconType) { this.beaconType = beaconType; }
}