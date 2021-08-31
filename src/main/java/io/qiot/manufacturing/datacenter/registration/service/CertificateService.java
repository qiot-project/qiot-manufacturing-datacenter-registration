/**
 * 
 */
package io.qiot.manufacturing.datacenter.registration.service;

import io.qiot.manufacturing.datacenter.commons.domain.registration.FactoryRegisterRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.MachineryRegisterRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.RegisterResponse;
import io.qiot.manufacturing.datacenter.registration.exception.CertificateProvisionException;

/**
 * @author mmascia
 * @author andreabattaglia
 *
 **/
public interface CertificateService {

    public RegisterResponse provisionFactory(
            FactoryRegisterRequest factoryRegisterRequest)
            throws CertificateProvisionException;

    public RegisterResponse provisionMachinery(
            MachineryRegisterRequest machineryRegisterRequest)
            throws CertificateProvisionException;
}
