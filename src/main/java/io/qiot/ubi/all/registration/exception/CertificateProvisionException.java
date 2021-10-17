/**
 * 
 */
package io.qiot.ubi.all.registration.exception;

/**
 * @author andreabattaglia
 *
 */
public class CertificateProvisionException extends Exception {

    /**
     * 
     */
    public CertificateProvisionException() {
        
    }

    /**
     * @param message
     */
    public CertificateProvisionException(String message) {
        super(message);
        
    }

    /**
     * @param cause
     */
    public CertificateProvisionException(Throwable cause) {
        super(cause);
        
    }

    /**
     * @param message
     * @param cause
     */
    public CertificateProvisionException(String message, Throwable cause) {
        super(message, cause);
        
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public CertificateProvisionException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        
    }

}
