/**
 * 
 */
package io.qiot.manufacturing.datacenter.registration.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import io.qiot.manufacturing.datacenter.commons.domain.registration.FactoryCertificateRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.MachineryCertificateRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.CertificateResponse;
import io.qiot.manufacturing.datacenter.registration.exception.CertificateProvisionException;
import io.qiot.manufacturing.datacenter.registration.service.CertificateService;

/**
 * @author andreabattaglia
 * @author mmascia
 *
 */
@ApplicationScoped
@Typed(DefaultCertificateService.class)
public class DefaultCertificateService implements CertificateService {

    final Logger LOGGER;

    public DefaultCertificateService(Logger log) {
        this.LOGGER = log;
    }

    @Override
    public CertificateResponse provisionFactory(FactoryCertificateRequest data)
            throws CertificateProvisionException {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream tsIs = loader
                .getResourceAsStream("certs/mqtts/client.ts");
                InputStream ksIs = loader
                        .getResourceAsStream("certs/mqtts/client.ks")) {
            LOGGER.debug("input stream of the Client KEY store: {}", ksIs);
            LOGGER.debug("input stream of the Client TRUST store: {}", tsIs);

            CertificateResponse registerResponse = new CertificateResponse();
            registerResponse.keystore = Base64.getEncoder()
                    .encodeToString(IOUtils.toByteArray(ksIs));
            registerResponse.truststore = Base64.getEncoder()
                    .encodeToString(IOUtils.toByteArray(tsIs));
            return registerResponse;
        } catch (IOException e) {
            throw new CertificateProvisionException(e);
        }
    }

    @Override
    public CertificateResponse provisionMachinery(MachineryCertificateRequest data)
            throws CertificateProvisionException {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream tsIs = loader
                .getResourceAsStream("certs/mqtts/client.ts");
                InputStream ksIs = loader
                        .getResourceAsStream("certs/mqtts/client.ks")) {
            LOGGER.debug("input stream of the Client KEY store: {}", ksIs);
            LOGGER.debug("input stream of the Client TRUST store: {}", tsIs);

            CertificateResponse registerResponse = new CertificateResponse();
            registerResponse.keystore = Base64.getEncoder()
                    .encodeToString(IOUtils.toByteArray(ksIs));
            registerResponse.truststore = Base64.getEncoder()
                    .encodeToString(IOUtils.toByteArray(tsIs));
            return registerResponse;
        } catch (IOException e) {
            throw new CertificateProvisionException(e);
        }
    }

}
