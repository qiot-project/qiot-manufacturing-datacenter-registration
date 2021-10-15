/**
 * 
 */
package io.qiot.manufacturing.datacenter.registration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Object containing the certificates returned by the registration service
 * 
 * @author andreabattaglia
 *
 */
@RegisterForReflection
public class CertificateResponse {
    @JsonProperty("truststore")
    public String truststore;
    @JsonProperty("keystore")
    public String keystore;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CertificateResponse [truststore=");
        builder.append(truststore);
        builder.append(", keystore=");
        builder.append(keystore);
        builder.append("]");
        return builder.toString();
    }

}
