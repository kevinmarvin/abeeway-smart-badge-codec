package com.github.kevinmarvin.abeeway.parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * High-level parameter configuration for Abeeway Smart Badge devices.
 * Provides a fluent API for setting device parameters with validation and documentation.
 */
public class AbeewayParams {
    
    private final Map<String, Object> parameters;
    
    private AbeewayParams(Map<String, Object> parameters) {
        this.parameters = new HashMap<>(parameters);
    }
    
    /**
     * Get the raw parameter map for encoding.
     */
    public Map<String, Object> getParameters() {
        return new HashMap<>(parameters);
    }
    
    /**
     * Get the value of a specific parameter.
     */
    public Object getParameter(String name) {
        return parameters.get(name);
    }
    
    /**
     * Get all parameter names that have been set.
     */
    public Set<String> getParameterNames() {
        return parameters.keySet();
    }
    
    /**
     * Create a new builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder class for constructing AbeewayParams with a fluent API.
     */
    public static class Builder {
        private final Map<String, Object> parameters = new HashMap<>();
        private final ParameterRegistry registry = ParameterRegistry.getInstance();
        
        // GPS and Location Parameters
        public Builder setGpsTimeout(int seconds) {
            return setParameter("gpsTimeout", seconds, 10, 300);
        }
        
        public Builder setGpsConvergenceTimeout(int seconds) {
            return setParameter("gpsConvergenceTimeout", seconds, 30, 300);
        }
        
        public Builder setGpsFixMinSatellites(int satellites) {
            return setParameter("gpsFixMinSatellites", satellites, 3, 12);
        }
        
        public Builder setGpsFixMinSnr(int snr) {
            return setParameter("gpsFixMinSnr", snr, 10, 50);
        }
        
        // Position Reporting Parameters
        public Builder setUplinkPeriod(int seconds) {
            return setParameter("uplinkPeriod", seconds, 60, 86400); // 1 min to 24 hours
        }
        
        public Builder setLorawanPeriod(int seconds) {
            return setParameter("lorawanPeriod", seconds, 300, 86400); // 5 min to 24 hours
        }
        
        public Builder setPositionOnDemandTimeout(int seconds) {
            return setParameter("positionOnDemandTimeout", seconds, 30, 300);
        }
        
        // Motion Detection Parameters
        public Builder setMotionSensitivity(MotionSensitivity sensitivity) {
            return setParameter("motionSensitivity", sensitivity.getValue(), 1, 5);
        }
        
        public Builder setMotionDebounceTime(int seconds) {
            return setParameter("motionDebounceTime", seconds, 1, 60);
        }
        
        public Builder setShockDetectionThreshold(int threshold) {
            return setParameter("shockDetectionThreshold", threshold, 100, 2000);
        }
        
        // WiFi Parameters
        public Builder setWifiScanTimeout(int seconds) {
            return setParameter("wifiScanTimeout", seconds, 5, 30);
        }
        
        public Builder setWifiMaxAccessPoints(int count) {
            return setParameter("wifiMaxAccessPoints", count, 1, 10);
        }
        
        // BLE Parameters
        public Builder setBleScanTimeout(int seconds) {
            return setParameter("bleScanTimeout", seconds, 5, 30);
        }
        
        public Builder setBleMaxBeacons(int count) {
            return setParameter("bleMaxBeacons", count, 1, 10);
        }
        
        // Battery and Power Parameters
        public Builder setBatteryLowThreshold(int percentage) {
            return setParameter("batteryLowThreshold", percentage, 5, 50);
        }
        
        public Builder setBatteryCriticalThreshold(int percentage) {
            return setParameter("batteryCriticalThreshold", percentage, 1, 20);
        }
        
        public Builder setPowerSaveMode(boolean enabled) {
            return setParameter("powerSaveMode", enabled ? 1 : 0, 0, 1);
        }
        
        // Activity Tracking Parameters
        public Builder setActivityTrackingEnabled(boolean enabled) {
            return setParameter("activityTrackingEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        public Builder setStepCounterEnabled(boolean enabled) {
            return setParameter("stepCounterEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        // Temperature Monitoring
        public Builder setTemperatureMonitoringEnabled(boolean enabled) {
            return setParameter("temperatureMonitoringEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        public Builder setTemperatureAlertThreshold(double celsius) {
            return setParameter("temperatureAlertThreshold", (int)(celsius * 10), -400, 850); // -40°C to 85°C
        }
        
        // Button Configuration
        public Builder setButtonPressEnabled(boolean enabled) {
            return setParameter("buttonPressEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        public Builder setSosButtonEnabled(boolean enabled) {
            return setParameter("sosButtonEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        public Builder setButtonLongPressThreshold(int milliseconds) {
            return setParameter("buttonLongPressThreshold", milliseconds, 500, 5000);
        }
        
        // LED and Buzzer Configuration
        public Builder setLedIndicationEnabled(boolean enabled) {
            return setParameter("ledIndicationEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        public Builder setBuzzerEnabled(boolean enabled) {
            return setParameter("buzzerEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        // Geofencing Parameters
        public Builder setGeofencingEnabled(boolean enabled) {
            return setParameter("geofencingEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        public Builder setGeofenceRadius(int meters) {
            return setParameter("geofenceRadius", meters, 10, 1000);
        }
        
        // Proximity Detection
        public Builder setProximityDetectionEnabled(boolean enabled) {
            return setParameter("proximityDetectionEnabled", enabled ? 1 : 0, 0, 1);
        }
        
        public Builder setProximityThreshold(int rssi) {
            return setParameter("proximityThreshold", rssi, -100, -30);
        }
        
        // Advanced Parameters
        public Builder setKeepAliveInterval(int seconds) {
            return setParameter("keepAliveInterval", seconds, 3600, 86400); // 1 hour to 24 hours
        }
        
        public Builder setRetransmissionAttempts(int attempts) {
            return setParameter("retransmissionAttempts", attempts, 1, 5);
        }
        
        public Builder setOperatingMode(OperatingMode mode) {
            return setParameter("operatingMode", mode.getValue(), 0, 6);
        }
        
        /**
         * Set a custom parameter by ID (for advanced users).
         */
        public Builder setCustomParameter(int parameterId, Object value) {
            return setParameter("custom_" + parameterId, value, null, null);
        }
        
        /**
         * Internal method to set and validate parameters.
         */
        private Builder setParameter(String name, Object value, Integer min, Integer max) {
            // Validate parameter if bounds are provided
            if (value instanceof Number && min != null && max != null) {
                int intValue = ((Number) value).intValue();
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException(
                        String.format("Parameter %s value %d is out of range [%d, %d]", 
                                    name, intValue, min, max));
                }
            }
            
            parameters.put(name, value);
            return this;
        }
        
        /**
         * Get a list of all available parameters with their descriptions.
         */
        public Map<String, String> getAvailableParameters() {
            return registry.getParameterDescriptions();
        }
        
        /**
         * Get parameter constraints (min/max values).
         */
        public ParameterConstraints getParameterConstraints(String parameterName) {
            return registry.getParameterConstraints(parameterName);
        }
        
        /**
         * Build the final AbeewayParams instance.
         */
        public AbeewayParams build() {
            return new AbeewayParams(parameters);
        }
    }
    
    /**
     * Motion sensitivity levels.
     */
    public enum MotionSensitivity {
        VERY_LOW(1),
        LOW(2),
        MEDIUM(3),
        HIGH(4),
        VERY_HIGH(5);
        
        private final int value;
        
        MotionSensitivity(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    /**
     * Operating modes for the device.
     */
    public enum OperatingMode {
        OFF(0),
        STANDBY(1),
        MOTION_TRACKING(2),
        PERMANENT_TRACKING(3),
        START_END_TRACKING(4),
        ACTIVITY_TRACKING(5),
        SOS_MODE(6);
        
        private final int value;
        
        OperatingMode(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
}