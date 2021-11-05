/**
 * 
 */
package io.qiot.ubi.all.registration.exception;

/**
 * @author mmascia
 *
 */
public class IssuerProvisionException extends Exception {

    /**
     * 
     */
    public IssuerProvisionException() {
        
    }

    /**
     * @param message
     */
    public IssuerProvisionException(String message) {
        super(message);
        
    }

    /**
     * @param cause
     */
    public IssuerProvisionException(Throwable cause) {
        super(cause);
        
    }

    /**
     * @param message
     * @param cause
     */
    public IssuerProvisionException(String message, Throwable cause) {
        super(message, cause);
        
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public IssuerProvisionException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        
    }

}
