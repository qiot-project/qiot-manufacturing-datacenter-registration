package io.qiot.manufacturing.datacenter.registration.domain;

import java.util.UUID;

/**
 * @author mmascia
 */
public class CertificateRequest {

    public CertificateRequest(UUID id, String name,
            String domain,
            String serial,
            String keyStorePassword) {
            
            this.id = id;
            this.name = name;
            this.domain = domain;
            this.serial = serial;
            this.keyStorePassword = keyStorePassword;
    }

    public UUID id;
    public String name;
    public String domain;
    public String serial;
    public String keyStorePassword;
    
}
