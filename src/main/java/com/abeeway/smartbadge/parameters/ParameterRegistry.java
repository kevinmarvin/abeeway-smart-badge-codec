package com.abeeway.smartbadge.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry containing parameter definitions, constraints, and metadata for Abeeway Smart Badge devices.
 */
public class ParameterRegistry {
    
    private static final ParameterRegistry INSTANCE = new ParameterRegistry();
    private final Map<String, ParameterDefinition> parameters = new HashMap<>();
    
    private ParameterRegistry() {
        initializeParameters();
    }
    
    public static ParameterRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Initialize all parameter definitions.
     */
    private void initializeParameters() {
        // GPS and Location Parameters
        addParameter("gpsTimeout", 0x01, "GPS acquisition timeout in seconds", 10, 300, "seconds");
        addParameter("gpsConvergenceTimeout", 0x02, "GPS convergence timeout in seconds", 30, 300, "seconds");
        addParameter("gpsFixMinSatellites", 0x03, "Minimum satellites required for GPS fix", 3, 12, "satellites");
        addParameter("gpsFixMinSnr", 0x04, "Minimum SNR required for GPS fix", 10, 50, "dB");
        
        // Position Reporting Parameters
        addParameter("uplinkPeriod", 0x10, "Uplink transmission period", 60, 86400, "seconds");
        addParameter("lorawanPeriod", 0x11, "LoRaWAN transmission period", 300, 86400, "seconds");
        addParameter("positionOnDemandTimeout", 0x12, "Position on demand timeout", 30, 300, "seconds");
        
        // Motion Detection Parameters
        addParameter("motionSensitivity", 0x20, "Motion detection sensitivity level", 1, 5, "level");
        addParameter("motionDebounceTime", 0x21, "Motion detection debounce time", 1, 60, "seconds");
        addParameter("shockDetectionThreshold", 0x22, "Shock detection threshold", 100, 2000, "mg");
        
        // WiFi Parameters
        addParameter("wifiScanTimeout", 0x30, "WiFi scan timeout", 5, 30, "seconds");
        addParameter("wifiMaxAccessPoints", 0x31, "Maximum WiFi access points to scan", 1, 10, "count");
        
        // BLE Parameters
        addParameter("bleScanTimeout", 0x40, "BLE scan timeout", 5, 30, "seconds");
        addParameter("bleMaxBeacons", 0x41, "Maximum BLE beacons to scan", 1, 10, "count");
        
        // Battery and Power Parameters
        addParameter("batteryLowThreshold", 0x50, "Battery low threshold", 5, 50, "percentage");
        addParameter("batteryCriticalThreshold", 0x51, "Battery critical threshold", 1, 20, "percentage");
        addParameter("powerSaveMode", 0x52, "Power save mode enabled", 0, 1, "boolean");
        
        // Activity Tracking Parameters
        addParameter("activityTrackingEnabled", 0x60, "Activity tracking enabled", 0, 1, "boolean");
        addParameter("stepCounterEnabled", 0x61, "Step counter enabled", 0, 1, "boolean");
        
        // Temperature Monitoring
        addParameter("temperatureMonitoringEnabled", 0x70, "Temperature monitoring enabled", 0, 1, "boolean");
        addParameter("temperatureAlertThreshold", 0x71, "Temperature alert threshold", -400, 850, "0.1°C");
        
        // Button Configuration
        addParameter("buttonPressEnabled", 0x80, "Button press detection enabled", 0, 1, "boolean");
        addParameter("sosButtonEnabled", 0x81, "SOS button enabled", 0, 1, "boolean");
        addParameter("buttonLongPressThreshold", 0x82, "Button long press threshold", 500, 5000, "milliseconds");
        
        // LED and Buzzer Configuration
        addParameter("ledIndicationEnabled", 0x90, "LED indication enabled", 0, 1, "boolean");
        addParameter("buzzerEnabled", 0x91, "Buzzer enabled", 0, 1, "boolean");
        
        // Geofencing Parameters
        addParameter("geofencingEnabled", 0xA0, "Geofencing enabled", 0, 1, "boolean");
        addParameter("geofenceRadius", 0xA1, "Geofence radius", 10, 1000, "meters");
        
        // Proximity Detection
        addParameter("proximityDetectionEnabled", 0xB0, "Proximity detection enabled", 0, 1, "boolean");
        addParameter("proximityThreshold", 0xB1, "Proximity RSSI threshold", -100, -30, "dBm");
        
        // Advanced Parameters
        addParameter("keepAliveInterval", 0xC0, "Keep alive interval", 3600, 86400, "seconds");
        addParameter("retransmissionAttempts", 0xC1, "Retransmission attempts", 1, 5, "attempts");
        addParameter("operatingMode", 0xC2, "Device operating mode", 0, 6, "mode");
    }
    
    /**
     * Add a parameter definition to the registry.
     */
    private void addParameter(String name, int id, String description, 
                             Integer minValue, Integer maxValue, String unit) {
        parameters.put(name, new ParameterDefinition(name, id, description, minValue, maxValue, unit));
    }
    
    /**
     * Get parameter definition by name.
     */
    public ParameterDefinition getParameter(String name) {
        return parameters.get(name);
    }
    
    /**
     * Get parameter constraints by name.
     */
    public ParameterConstraints getParameterConstraints(String name) {
        ParameterDefinition def = parameters.get(name);
        if (def == null) {
            return null;
        }
        return new ParameterConstraints(def.getMinValue(), def.getMaxValue(), def.getUnit());
    }
    
    /**
     * Get all parameter descriptions.
     */
    public Map<String, String> getParameterDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        for (ParameterDefinition def : parameters.values()) {
            String desc = def.getDescription();
            if (def.getMinValue() != null && def.getMaxValue() != null) {
                desc += String.format(" (Range: %d-%d %s)", 
                                    def.getMinValue(), def.getMaxValue(), def.getUnit());
            }
            descriptions.put(def.getName(), desc);
        }
        return descriptions;
    }
    
    /**
     * Get parameter ID by name.
     */
    public Integer getParameterId(String name) {
        ParameterDefinition def = parameters.get(name);
        return def != null ? def.getId() : null;
    }
    
    /**
     * Check if a parameter exists.
     */
    public boolean hasParameter(String name) {
        return parameters.containsKey(name);
    }
    
    /**
     * Get all parameter names.
     */
    public java.util.Set<String> getParameterNames() {
        return parameters.keySet();
    }
    
    /**
     * Parameter definition class.
     */
    public static class ParameterDefinition {
        private final String name;
        private final int id;
        private final String description;
        private final Integer minValue;
        private final Integer maxValue;
        private final String unit;
        
        public ParameterDefinition(String name, int id, String description, 
                                 Integer minValue, Integer maxValue, String unit) {
            this.name = name;
            this.id = id;
            this.description = description;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.unit = unit;
        }
        
        public String getName() { return name; }
        public int getId() { return id; }
        public String getDescription() { return description; }
        public Integer getMinValue() { return minValue; }
        public Integer getMaxValue() { return maxValue; }
        public String getUnit() { return unit; }
    }
}