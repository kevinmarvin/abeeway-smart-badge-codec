package com.abeeway.smartbadge.models;

import com.abeeway.smartbadge.enums.*;
import java.util.Map;
import java.util.List;

/**
 * Main data class containing all decoded uplink message information from an Abeeway Smart Badge device.
 */
public class UplinkData {
    // Core message fields
    private MessageType messageType;
    private String payload;
    private Integer ackToken;
    private String trackingMode;
    private Boolean onDemand;
    private Boolean periodicPosition;
    private DynamicMotionState dynamicMotionState;
    private Integer appState;
    private Integer sosFlag;
    
    // Battery and health
    private Integer batteryLevel;
    private BatteryStatus batteryStatus;
    private Double batteryVoltage;
    private Double temperatureMeasure;
    
    // Position fields
    private RawPositionType rawPositionType;
    private Double gpsLatitude;
    private Double gpsLongitude;
    private Object horizontalAccuracy;
    private Integer age;
    private GpsFixStatus gpsFixStatus;
    
    // WiFi positioning
    private List<WiFiAccessPoint> wifiAccessPoints;
    private Integer wifiFailure;
    private TimeoutCause wifiTimeoutCause;
    
    // BLE positioning  
    private List<BleBeacon> bleBeacons;
    private BleBeaconFailure bleBeaconFailure;
    private Integer bleFailure;
    
    // Event data
    private EventType eventType;
    private String eventData;
    
    // Configuration
    private Map<String, Object> deviceConfiguration;
    
    // Activity and motion
    private Integer stepCount;
    private Double activityCounter;
    private Boolean motionDetected;
    
    // Debug and diagnostic
    private DebugCommandType debugCommandType;
    private String debugData;
    private List<ErrorCode> errorCodes;
    
    // Shutdown information
    private ShutdownCause shutdownCause;
    private String shutdownData;
    
    // Proximity and collection data
    private List<ProximityData> proximityData;
    private CollectionScanType collectionScanType;
    private Object collectionData;
    
    // Miscellaneous data
    private Map<MiscDataTag, Object> miscData;
    
    // Constructors
    public UplinkData() {
    }
    
    // Getters and Setters
    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }
    
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    
    public Integer getAckToken() { return ackToken; }
    public void setAckToken(Integer ackToken) { this.ackToken = ackToken; }
    
    public String getTrackingMode() { return trackingMode; }
    public void setTrackingMode(String trackingMode) { this.trackingMode = trackingMode; }
    
    public Boolean getOnDemand() { return onDemand; }
    public void setOnDemand(Boolean onDemand) { this.onDemand = onDemand; }
    
    public Boolean getPeriodicPosition() { return periodicPosition; }
    public void setPeriodicPosition(Boolean periodicPosition) { this.periodicPosition = periodicPosition; }
    
    public DynamicMotionState getDynamicMotionState() { return dynamicMotionState; }
    public void setDynamicMotionState(DynamicMotionState dynamicMotionState) { this.dynamicMotionState = dynamicMotionState; }
    
    public Integer getAppState() { return appState; }
    public void setAppState(Integer appState) { this.appState = appState; }
    
    public Integer getSosFlag() { return sosFlag; }
    public void setSosFlag(Integer sosFlag) { this.sosFlag = sosFlag; }
    
    public Integer getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Integer batteryLevel) { this.batteryLevel = batteryLevel; }
    
    public BatteryStatus getBatteryStatus() { return batteryStatus; }
    public void setBatteryStatus(BatteryStatus batteryStatus) { this.batteryStatus = batteryStatus; }
    
    public Double getBatteryVoltage() { return batteryVoltage; }
    public void setBatteryVoltage(Double batteryVoltage) { this.batteryVoltage = batteryVoltage; }
    
    public Double getTemperatureMeasure() { return temperatureMeasure; }
    public void setTemperatureMeasure(Double temperatureMeasure) { this.temperatureMeasure = temperatureMeasure; }
    
    public RawPositionType getRawPositionType() { return rawPositionType; }
    public void setRawPositionType(RawPositionType rawPositionType) { this.rawPositionType = rawPositionType; }
    
    public Double getGpsLatitude() { return gpsLatitude; }
    public void setGpsLatitude(Double gpsLatitude) { this.gpsLatitude = gpsLatitude; }
    
    public Double getGpsLongitude() { return gpsLongitude; }
    public void setGpsLongitude(Double gpsLongitude) { this.gpsLongitude = gpsLongitude; }
    
    public Object getHorizontalAccuracy() { return horizontalAccuracy; }
    public void setHorizontalAccuracy(Object horizontalAccuracy) { this.horizontalAccuracy = horizontalAccuracy; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public GpsFixStatus getGpsFixStatus() { return gpsFixStatus; }
    public void setGpsFixStatus(GpsFixStatus gpsFixStatus) { this.gpsFixStatus = gpsFixStatus; }
    
    public List<WiFiAccessPoint> getWifiAccessPoints() { return wifiAccessPoints; }
    public void setWifiAccessPoints(List<WiFiAccessPoint> wifiAccessPoints) { this.wifiAccessPoints = wifiAccessPoints; }
    
    public Integer getWifiFailure() { return wifiFailure; }
    public void setWifiFailure(Integer wifiFailure) { this.wifiFailure = wifiFailure; }
    
    public TimeoutCause getWifiTimeoutCause() { return wifiTimeoutCause; }
    public void setWifiTimeoutCause(TimeoutCause wifiTimeoutCause) { this.wifiTimeoutCause = wifiTimeoutCause; }
    
    public List<BleBeacon> getBleBeacons() { return bleBeacons; }
    public void setBleBeacons(List<BleBeacon> bleBeacons) { this.bleBeacons = bleBeacons; }
    
    public BleBeaconFailure getBleBeaconFailure() { return bleBeaconFailure; }
    public void setBleBeaconFailure(BleBeaconFailure bleBeaconFailure) { this.bleBeaconFailure = bleBeaconFailure; }
    
    public Integer getBleFailure() { return bleFailure; }
    public void setBleFailure(Integer bleFailure) { this.bleFailure = bleFailure; }
    
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    
    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }
    
    public Map<String, Object> getDeviceConfiguration() { return deviceConfiguration; }
    public void setDeviceConfiguration(Map<String, Object> deviceConfiguration) { this.deviceConfiguration = deviceConfiguration; }
    
    public Integer getStepCount() { return stepCount; }
    public void setStepCount(Integer stepCount) { this.stepCount = stepCount; }
    
    public Double getActivityCounter() { return activityCounter; }
    public void setActivityCounter(Double activityCounter) { this.activityCounter = activityCounter; }
    
    public Boolean getMotionDetected() { return motionDetected; }
    public void setMotionDetected(Boolean motionDetected) { this.motionDetected = motionDetected; }
    
    public DebugCommandType getDebugCommandType() { return debugCommandType; }
    public void setDebugCommandType(DebugCommandType debugCommandType) { this.debugCommandType = debugCommandType; }
    
    public String getDebugData() { return debugData; }
    public void setDebugData(String debugData) { this.debugData = debugData; }
    
    public List<ErrorCode> getErrorCodes() { return errorCodes; }
    public void setErrorCodes(List<ErrorCode> errorCodes) { this.errorCodes = errorCodes; }
    
    public ShutdownCause getShutdownCause() { return shutdownCause; }
    public void setShutdownCause(ShutdownCause shutdownCause) { this.shutdownCause = shutdownCause; }
    
    public String getShutdownData() { return shutdownData; }
    public void setShutdownData(String shutdownData) { this.shutdownData = shutdownData; }
    
    public List<ProximityData> getProximityData() { return proximityData; }
    public void setProximityData(List<ProximityData> proximityData) { this.proximityData = proximityData; }
    
    public CollectionScanType getCollectionScanType() { return collectionScanType; }
    public void setCollectionScanType(CollectionScanType collectionScanType) { this.collectionScanType = collectionScanType; }
    
    public Object getCollectionData() { return collectionData; }
    public void setCollectionData(Object collectionData) { this.collectionData = collectionData; }
    
    public Map<MiscDataTag, Object> getMiscData() { return miscData; }
    public void setMiscData(Map<MiscDataTag, Object> miscData) { this.miscData = miscData; }
}