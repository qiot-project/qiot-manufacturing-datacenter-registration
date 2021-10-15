package io.qiot.manufacturing.datacenter.registration.domain;

import java.util.UUID;

/**
 * @author mmascia
 * @author andreabattaglia
 */
public class CertificateRequest {

    public CertificateRequest(UUID id, 
            String name,
            String domain,
            String serial,
            String keyStorePassword,
            boolean isAC) {
            
            this.id = id;
            this.name = name;
            this.domain = domain;
            this.serial = serial;
            this.keyStorePassword = keyStorePassword;
            this.ac = isAC;
    }

    public UUID id;
    public String name;
    public String domain;
    public String serial;
    public String keyStorePassword;
    public boolean ac;
    
}
