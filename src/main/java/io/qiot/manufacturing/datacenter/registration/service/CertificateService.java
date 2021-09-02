/**
 * 
 */
package io.qiot.manufacturing.datacenter.registration.service;

import io.qiot.manufacturing.datacenter.commons.domain.registration.CertificateResponse;
import io.qiot.manufacturing.datacenter.commons.domain.registration.FactoryCertificateRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.MachineryCertificateRequest;
import io.qiot.manufacturing.datacenter.registration.exception.CertificateProvisionException;

/**
 * @author mmascia
 * @author andreabattaglia
 *
 **/
public interface CertificateService {

    public CertificateResponse provisionFactory(
            FactoryCertificateRequest factoryRegisterRequest)
            throws CertificateProvisionException;

    public CertificateResponse provisionMachinery(
            MachineryCertificateRequest machineryRegisterRequest)
            throws CertificateProvisionException;
}
