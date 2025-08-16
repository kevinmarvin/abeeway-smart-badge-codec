package com.github.kevinmarvin.abeeway.parameters;

/**
 * Represents constraints for a parameter including valid ranges and units.
 */
public class ParameterConstraints {
    
    private final Integer minValue;
    private final Integer maxValue;
    private final String unit;
    
    public ParameterConstraints(Integer minValue, Integer maxValue, String unit) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.unit = unit;
    }
    
    /**
     * Get the minimum allowed value for this parameter.
     */
    public Integer getMinValue() {
        return minValue;
    }
    
    /**
     * Get the maximum allowed value for this parameter.
     */
    public Integer getMaxValue() {
        return maxValue;
    }
    
    /**
     * Get the unit of measurement for this parameter.
     */
    public String getUnit() {
        return unit;
    }
    
    /**
     * Check if a value is within the valid range for this parameter.
     */
    public boolean isValid(int value) {
        if (minValue != null && value < minValue) {
            return false;
        }
        if (maxValue != null && value > maxValue) {
            return false;
        }
        return true;
    }
    
    /**
     * Get a human-readable description of the constraints.
     */
    public String getDescription() {
        if (minValue != null && maxValue != null) {
            return String.format("Range: %d-%d %s", minValue, maxValue, unit);
        } else if (minValue != null) {
            return String.format("Minimum: %d %s", minValue, unit);
        } else if (maxValue != null) {
            return String.format("Maximum: %d %s", maxValue, unit);
        } else {
            return "No constraints";
        }
    }
    
    @Override
    public String toString() {
        return getDescription();
    }
}