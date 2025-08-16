package com.github.kevinmarvin.abeeway.models;

/**
 * Data class representing proximity detection data.
 */
public class ProximityData {
    private String deviceId;
    private Integer rssi;
    private Double distance;
    private String proximityType;
    private Long timestamp;
    
    public ProximityData() {
    }
    
    public ProximityData(String deviceId, Integer rssi) {
        this.deviceId = deviceId;
        this.rssi = rssi;
    }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public Integer getRssi() { return rssi; }
    public void setRssi(Integer rssi) { this.rssi = rssi; }
    
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    
    public String getProximityType() { return proximityType; }
    public void setProximityType(String proximityType) { this.proximityType = proximityType; }
    
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}