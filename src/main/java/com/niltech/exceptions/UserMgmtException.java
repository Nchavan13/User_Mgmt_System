package com.niltech.exceptions;

public class UserMgmtException extends RuntimeException {
    
    public UserMgmtException(String message) {
        super(message);
    }

    public UserMgmtException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
