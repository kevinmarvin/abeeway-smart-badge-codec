package com.github.kevinmarvin.abeeway.models;

/**
 * Data class representing a WiFi access point detected during position scanning.
 */
public class WiFiAccessPoint {
    private String macAddress;
    private Integer rssi;
    private String ssid;
    private Integer channel;
    private Boolean encrypted;
    
    public WiFiAccessPoint() {
    }
    
    public WiFiAccessPoint(String macAddress, Integer rssi) {
        this.macAddress = macAddress;
        this.rssi = rssi;
    }
    
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    
    public Integer getRssi() { return rssi; }
    public void setRssi(Integer rssi) { this.rssi = rssi; }
    
    public String getSsid() { return ssid; }
    public void setSsid(String ssid) { this.ssid = ssid; }
    
    public Integer getChannel() { return channel; }
    public void setChannel(Integer channel) { this.channel = channel; }
    
    public Boolean getEncrypted() { return encrypted; }
    public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }
}