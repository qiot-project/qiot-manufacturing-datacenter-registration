/**
 * 
 */
package io.qiot.ubi.all.registration.service;

import io.qiot.ubi.all.registration.domain.CertificateRequest;
import io.qiot.ubi.all.registration.domain.CertificateResponse;
import io.qiot.ubi.all.registration.exception.CertificateProvisionException;

/**
 * @author mmascia
 * @author andreabattaglia
 *
 **/
public interface CertificateService {

    public CertificateResponse provision(
        CertificateRequest certificateRequest)
        throws CertificateProvisionException;
}
