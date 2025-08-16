package com.github.kevinmarvin.abeeway.models;

import java.util.List;

/**
 * Container for decoded downlink command data from an Abeeway Smart Badge device.
 */
public class DecodedDownlink {
    private Object data;
    private List<String> errors;
    private List<String> warnings;

    public DecodedDownlink() {
    }

    public DecodedDownlink(Object data, List<String> errors, List<String> warnings) {
        this.data = data;
        this.errors = errors;
        this.warnings = warnings;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}