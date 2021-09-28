/**
 * 
 */
package io.qiot.manufacturing.datacenter.registration.service;

import io.qiot.manufacturing.datacenter.commons.domain.registration.CertificateResponse;
import io.qiot.manufacturing.datacenter.registration.domain.CertificateRequest;
import io.qiot.manufacturing.datacenter.registration.exception.CertificateProvisionException;

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
