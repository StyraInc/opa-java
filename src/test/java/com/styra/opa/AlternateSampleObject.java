package com.styra.opa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class AlternateSampleObject {
    @JsonProperty
    private Map<String, java.lang.Object> nestedMap;

    @JsonProperty
    private String stringVal;

    public Map<String, java.lang.Object> getNestedMap() {
        return nestedMap;
    }

    public void setNestedMap(Map<String, java.lang.Object> newValue) {
        nestedMap = newValue;
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setStringVal(String newValue) {
        stringVal = newValue;
    }

}
