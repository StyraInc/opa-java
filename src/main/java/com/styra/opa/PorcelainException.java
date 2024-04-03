package com.styra.opa;

// TODO: select an appropriate name and move this into the SDK
//
// This generic exception should be used as a parent type for any other custom
// exceptions thrown by the high level API.
public class PorcelainException extends Exception {

    public PorcelainException(Throwable cause) {
        super(cause);
    }

    public PorcelainException(String message) {
        super (message);
    }

    public PorcelainException(String message, Throwable cause) {
        super(message, cause);
    }
}
