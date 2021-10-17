/**
 * 
 */
package io.qiot.manufacturing.datacenter.registration.service;

import io.qiot.manufacturing.datacenter.registration.exception.CertificateProvisionException;
import io.qiot.ubi.all.registration.domain.CertificateRequest;
import io.qiot.ubi.all.registration.domain.CertificateResponse;

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
