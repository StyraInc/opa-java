/* 
 * Code generated by Speakeasy (https://speakeasy.com). DO NOT EDIT.
 */

package com.styra.opa.openapi.models.shared;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.styra.opa.openapi.utils.Utils;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Objects;
import java.util.Optional;


public class ResponsesErrors {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("location")
    private Optional<? extends ResponsesLocation> location;

    @JsonCreator
    public ResponsesErrors(
            @JsonProperty("code") String code,
            @JsonProperty("message") String message,
            @JsonProperty("location") Optional<? extends ResponsesLocation> location) {
        Utils.checkNotNull(code, "code");
        Utils.checkNotNull(message, "message");
        Utils.checkNotNull(location, "location");
        this.code = code;
        this.message = message;
        this.location = location;
    }
    
    public ResponsesErrors(
            String code,
            String message) {
        this(code, message, Optional.empty());
    }

    @JsonIgnore
    public String code() {
        return code;
    }

    @JsonIgnore
    public String message() {
        return message;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Optional<ResponsesLocation> location() {
        return (Optional<ResponsesLocation>) location;
    }

    public final static Builder builder() {
        return new Builder();
    }

    public ResponsesErrors withCode(String code) {
        Utils.checkNotNull(code, "code");
        this.code = code;
        return this;
    }

    public ResponsesErrors withMessage(String message) {
        Utils.checkNotNull(message, "message");
        this.message = message;
        return this;
    }

    public ResponsesErrors withLocation(ResponsesLocation location) {
        Utils.checkNotNull(location, "location");
        this.location = Optional.ofNullable(location);
        return this;
    }

    public ResponsesErrors withLocation(Optional<? extends ResponsesLocation> location) {
        Utils.checkNotNull(location, "location");
        this.location = location;
        return this;
    }
    
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponsesErrors other = (ResponsesErrors) o;
        return 
            Objects.deepEquals(this.code, other.code) &&
            Objects.deepEquals(this.message, other.message) &&
            Objects.deepEquals(this.location, other.location);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
            code,
            message,
            location);
    }
    
    @Override
    public String toString() {
        return Utils.toString(ResponsesErrors.class,
                "code", code,
                "message", message,
                "location", location);
    }
    
    public final static class Builder {
 
        private String code;
 
        private String message;
 
        private Optional<? extends ResponsesLocation> location = Optional.empty();  
        
        private Builder() {
          // force use of static builder() method
        }

        public Builder code(String code) {
            Utils.checkNotNull(code, "code");
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            Utils.checkNotNull(message, "message");
            this.message = message;
            return this;
        }

        public Builder location(ResponsesLocation location) {
            Utils.checkNotNull(location, "location");
            this.location = Optional.ofNullable(location);
            return this;
        }

        public Builder location(Optional<? extends ResponsesLocation> location) {
            Utils.checkNotNull(location, "location");
            this.location = location;
            return this;
        }
        
        public ResponsesErrors build() {
            return new ResponsesErrors(
                code,
                message,
                location);
        }
    }
}
