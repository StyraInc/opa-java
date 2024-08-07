/* 
 * Code generated by Speakeasy (https://speakeasy.com). DO NOT EDIT.
 */

package com.styra.opa.openapi.models.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.styra.opa.openapi.utils.Utils;
import java.io.InputStream;
import java.lang.Deprecated;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.styra.opa.openapi.utils.TypedObject;
import com.styra.opa.openapi.utils.Utils.JsonShape;

/**
 * Input - Arbitrary JSON used within your policies by accessing `input`
 */

@JsonDeserialize(using = Input._Deserializer.class)
public class Input {

    @com.fasterxml.jackson.annotation.JsonValue
    private TypedObject value;
    
    private Input(TypedObject value) {
        this.value = value;
    }

    public static Input of(boolean value) {
        Utils.checkNotNull(value, "value");
        return new Input(TypedObject.of(value, JsonShape.DEFAULT, new TypeReference<Boolean>(){}));
    }

    public static Input of(String value) {
        Utils.checkNotNull(value, "value");
        return new Input(TypedObject.of(value, JsonShape.DEFAULT, new TypeReference<String>(){}));
    }

    public static Input of(double value) {
        Utils.checkNotNull(value, "value");
        return new Input(TypedObject.of(value, JsonShape.DEFAULT, new TypeReference<Double>(){}));
    }

    public static Input of(java.util.List<java.lang.Object> value) {
        Utils.checkNotNull(value, "value");
        return new Input(TypedObject.of(value, JsonShape.DEFAULT, new TypeReference<java.util.List<java.lang.Object>>(){}));
    }

    public static Input of(java.util.Map<String, java.lang.Object> value) {
        Utils.checkNotNull(value, "value");
        return new Input(TypedObject.of(value, JsonShape.DEFAULT, new TypeReference<java.util.Map<String, java.lang.Object>>(){}));
    }
    
    /**
     * Returns an instance of one of these types:
     * <ul>
     * <li>{@code boolean}</li>
     * <li>{@code String}</li>
     * <li>{@code double}</li>
     * <li>{@code java.util.List<java.lang.Object>}</li>
     * <li>{@code java.util.Map<String, java.lang.Object>}</li>
     * </ul>
     * 
     * <p>Use {@code instanceof} to determine what type is returned. For example:
     * 
     * <pre>
     * if (obj.value() instanceof String) {
     *     String answer = (String) obj.value();
     *     System.out.println("answer=" + answer);
     * }
     * </pre>
     * 
     * @return value of oneOf type
     **/ 
    public java.lang.Object value() {
        return value.value();
    }    
    
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Input other = (Input) o;
        return java.util.Objects.deepEquals(this.value.value(), other.value.value()); 
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(value.value());
    }
    
    @SuppressWarnings("serial")
    public static final class _Deserializer extends com.styra.opa.openapi.utils.OneOfDeserializer<Input> {

        public _Deserializer() {
            super(Input.class,
                  Utils.TypeReferenceWithShape.of(new TypeReference<Boolean>() {}, Utils.JsonShape.DEFAULT),
                  Utils.TypeReferenceWithShape.of(new TypeReference<String>() {}, Utils.JsonShape.DEFAULT),
                  Utils.TypeReferenceWithShape.of(new TypeReference<Double>() {}, Utils.JsonShape.DEFAULT),
                  Utils.TypeReferenceWithShape.of(new TypeReference<java.util.List<java.lang.Object>>() {}, Utils.JsonShape.DEFAULT),
                  Utils.TypeReferenceWithShape.of(new TypeReference<java.util.Map<String, java.lang.Object>>() {}, Utils.JsonShape.DEFAULT));
        }
    }
    
    @Override
    public String toString() {
        return Utils.toString(Input.class,
                "value", value);
    }
 
}
