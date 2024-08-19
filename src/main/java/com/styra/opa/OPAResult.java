package com.styra.opa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class encapsulates a result of a previous OPA request for deferred
 * access. It may contain an exception, a value, or both. This is intended for
 * usage patterns where multiple requests are made, and only later does the
 * caller want to inspect which of them succeeded or failed, and what their
 * values may have been.
 *
 * This abstraction is a little unclean - typically it is preferred to throw
 * exceptions, not pass them around as values. However, problems with the
 * HTTP requests often generate exceptions, and those can be challenging to
 * deal with when dispatching many requests at once.
 */
public class OPAResult {

    private Object value;
    private OPAException exception;
    private ObjectMapper om;

    public OPAResult(Object newValue, OPAException newException) {
        this.value = newValue;
        this.exception = newException;
        this.om = new ObjectMapper();
    }

    public OPAResult(Object newValue) {
        this.value = newValue;
        this.exception = null;
        this.om = new ObjectMapper();
    }

    /**
     * Attempt to retrieve the value stored in this result, throwing the stored
     * exception if one is present.
     *
     * NOTE: this can make it look like the exception came from this class, but
     * that's not true, it was simply smuggling a deferred exception from
     * somewhere else inside of it. OPAException usually wraps some other,
     * lower-level exception - consider inspecting that to determine the true
     * source of error.
     * @throws OPAException
     */
    public <T> T get() throws OPAException {
        if (this.exception != null) {
            throw this.exception;
        }

        T typedResult = this.om.convertValue(this.value, new TypeReference<T>() {});

        return typedResult;
    }

    public <T> T get(Class<T> toValueType) throws OPAException {
        if (this.exception != null) {
            throw this.exception;
        }

        T typedResult = this.om.convertValue(this.value, toValueType);

        return typedResult;
    }

    public <T> T get(JavaType toValueType) throws OPAException {
        if (this.exception != null) {
            throw this.exception;
        }

        T typedResult = this.om.convertValue(this.value, toValueType);

        return typedResult;
    }

    public <T> T get(TypeReference<T> toValueType) throws OPAException {
        if (this.exception != null) {
            throw this.exception;
        }

        T typedResult = this.om.convertValue(this.value, toValueType);

        return typedResult;
    }

    /**
     * Returns true if and only if this result contains a non-null value AND ta
     * null exception. In other words, if this method returns true, then get()
     * is guaranteed not to throw an exception or return null.
     */
    public boolean success() {
        if (this.exception != null) {
            return false;
        }
        if (this.value == null) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves the deferred value if one is present. Does not attempt to
     * re-throw a stored exception even if one is present.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Retrieves the deferred exception, if any.
     */
    public OPAException getException() {
        return this.exception;
    }

}
