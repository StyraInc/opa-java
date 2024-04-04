package com.styra.opa;

// TODO: select an appropriate name and move this into the SDK
//
// This generic exception should be used as a parent type for any other custom
// exceptions thrown by the high level API.
public class OPAException extends Exception {

    public OPAException(Throwable cause) {
        super(cause);
    }

    public OPAException(String message) {
        super (message);
    }

    public OPAException(String message, Throwable cause) {
        super(message, cause);
    }
}
