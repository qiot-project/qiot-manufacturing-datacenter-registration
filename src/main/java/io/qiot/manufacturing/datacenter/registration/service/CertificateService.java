/**
 * 
 */
package io.qiot.manufacturing.datacenter.registration.service;

import io.qiot.manufacturing.datacenter.commons.domain.registration.FactoryCertificateRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.MachineryCertificateRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.RegisterResponse;
import io.qiot.manufacturing.datacenter.registration.exception.CertificateProvisionException;

/**
 * @author mmascia
 * @author andreabattaglia
 *
 **/
public interface CertificateService {

    public RegisterResponse provisionFactory(
            FactoryCertificateRequest factoryRegisterRequest)
            throws CertificateProvisionException;

    public RegisterResponse provisionMachinery(
            MachineryCertificateRequest machineryRegisterRequest)
            throws CertificateProvisionException;
}
