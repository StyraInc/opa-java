/* 
 * Code generated by Speakeasy (https://speakeasy.com). DO NOT EDIT.
 */

package com.styra.opa.openapi.models.shared;


import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.styra.opa.openapi.utils.OneOfDeserializer;
import com.styra.opa.openapi.utils.TypedObject;
import com.styra.opa.openapi.utils.Utils.JsonShape;
import com.styra.opa.openapi.utils.Utils.TypeReferenceWithShape;
import com.styra.opa.openapi.utils.Utils;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Objects;


@JsonDeserialize(using = Responses._Deserializer.class)
public class Responses {

    @JsonValue
    private TypedObject value;
    
    private Responses(TypedObject value) {
        this.value = value;
    }

    public static Responses of(ResponsesSuccessfulPolicyResponse value) {
        Utils.checkNotNull(value, "value");
        return new Responses(TypedObject.of(value, JsonShape.DEFAULT, new TypeReference<ResponsesSuccessfulPolicyResponse>(){}));
    }

    public static Responses of(ServerError value) {
        Utils.checkNotNull(value, "value");
        return new Responses(TypedObject.of(value, JsonShape.DEFAULT, new TypeReference<ServerError>(){}));
    }
    
    /**
     * Returns an instance of one of these types:
     * <ul>
     * <li>{@code com.styra.opa.openapi.models.shared.ResponsesSuccessfulPolicyResponse}</li>
     * <li>{@code com.styra.opa.openapi.models.shared.ServerError}</li>
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
        Responses other = (Responses) o;
        return Objects.deepEquals(this.value.value(), other.value.value()); 
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value.value());
    }
    
    @SuppressWarnings("serial")
    public static final class _Deserializer extends OneOfDeserializer<Responses> {

        public _Deserializer() {
            super(Responses.class,
                  TypeReferenceWithShape.of(new TypeReference<com.styra.opa.openapi.models.shared.ResponsesSuccessfulPolicyResponse>() {}, JsonShape.DEFAULT),
                  TypeReferenceWithShape.of(new TypeReference<com.styra.opa.openapi.models.shared.ServerError>() {}, JsonShape.DEFAULT));
        }
    }
    
    @Override
    public String toString() {
        return Utils.toString(Responses.class,
                "value", value);
    }
 
}