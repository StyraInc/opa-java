/* 
 * Code generated by Speakeasy (https://speakeasy.com). DO NOT EDIT.
 */
package com.styra.opa.openapi.models.shared;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.styra.opa.openapi.utils.Utils;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * CompileResultMultitargetResult
 * 
 * <p>The partially evaluated result of the query in each target dialect.
 */
public class CompileResultMultitargetResult {

    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("targets")
    private Optional<? extends Targets> targets;

    @JsonIgnore
    private Map<String, Object> additionalProperties;

    @JsonCreator
    public CompileResultMultitargetResult(
            @JsonProperty("targets") Optional<? extends Targets> targets) {
        Utils.checkNotNull(targets, "targets");
        this.targets = targets;
        this.additionalProperties = new HashMap<>();
    }
    
    public CompileResultMultitargetResult() {
        this(Optional.empty());
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Optional<Targets> targets() {
        return (Optional<Targets>) targets;
    }

    @JsonAnyGetter
    public Map<String, Object> additionalProperties() {
        return additionalProperties;
    }

    public final static Builder builder() {
        return new Builder();
    }    

    public CompileResultMultitargetResult withTargets(Targets targets) {
        Utils.checkNotNull(targets, "targets");
        this.targets = Optional.ofNullable(targets);
        return this;
    }

    public CompileResultMultitargetResult withTargets(Optional<? extends Targets> targets) {
        Utils.checkNotNull(targets, "targets");
        this.targets = targets;
        return this;
    }

    @JsonAnySetter
    public CompileResultMultitargetResult withAdditionalProperty(String key, Object value) {
        // note that value can be null because of the way JsonAnySetter works
        Utils.checkNotNull(key, "key");
        additionalProperties.put(key, value); 
        return this;
    }    

    public CompileResultMultitargetResult withAdditionalProperties(Map<String, Object> additionalProperties) {
        Utils.checkNotNull(additionalProperties, "additionalProperties");
        this.additionalProperties = additionalProperties;
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
        CompileResultMultitargetResult other = (CompileResultMultitargetResult) o;
        return 
            Objects.deepEquals(this.targets, other.targets) &&
            Objects.deepEquals(this.additionalProperties, other.additionalProperties);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
            targets,
            additionalProperties);
    }
    
    @Override
    public String toString() {
        return Utils.toString(CompileResultMultitargetResult.class,
                "targets", targets,
                "additionalProperties", additionalProperties);
    }
    
    public final static class Builder {
 
        private Optional<? extends Targets> targets = Optional.empty();
 
        private Map<String, Object> additionalProperties = new HashMap<>();
        
        private Builder() {
          // force use of static builder() method
        }

        public Builder targets(Targets targets) {
            Utils.checkNotNull(targets, "targets");
            this.targets = Optional.ofNullable(targets);
            return this;
        }

        public Builder targets(Optional<? extends Targets> targets) {
            Utils.checkNotNull(targets, "targets");
            this.targets = targets;
            return this;
        }

        public Builder additionalProperty(String key, Object value) {
            Utils.checkNotNull(key, "key");
            // we could be strict about null values (force the user
            // to pass `JsonNullable.of(null)`) but likely to be a bit 
            // annoying for additional properties building so we'll 
            // relax preconditions.
            this.additionalProperties.put(key, value);
            return this;
        }

        public Builder additionalProperties(Map<String, Object> additionalProperties) {
            Utils.checkNotNull(additionalProperties, "additionalProperties");
            this.additionalProperties = additionalProperties;
            return this;
        }
        
        public CompileResultMultitargetResult build() {
            return new CompileResultMultitargetResult(
                targets)
                .withAdditionalProperties(additionalProperties);
        }
    }
}
