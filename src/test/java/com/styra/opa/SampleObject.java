package com.styra.opa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SampleObject<T> {

    @JsonProperty
    private boolean boolProperty;

    @JsonProperty
    private int intProperty;

    @JsonProperty
    private T customProperty;

    @JsonProperty("customAnnotatedProperty")
    private int annotatedProperty;

    @JsonProperty
    private Map<String, String> mapProperty;

    public boolean getBoolProperty() {
        return boolProperty;
    }

    public void setBoolProperty(boolean newValue) {
        boolProperty = newValue;
    }

    public int getIntProperty() {
        return intProperty;
    }

    public void setIntProperty(int newValue) {
        intProperty = newValue;
    }

    public T getCustomProperty() {
        return customProperty;
    }

    public void setCustomProperty(T newValue) {
        customProperty = newValue;
    }

    public int getAnnotatedProperty() {
        return annotatedProperty;
    }

    public void setAnnotatedProperty(int newValue) {
        annotatedProperty = newValue;
    }

    public Map<String, String> getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map<String, String> newValue) {
        mapProperty = newValue;
    }

}
