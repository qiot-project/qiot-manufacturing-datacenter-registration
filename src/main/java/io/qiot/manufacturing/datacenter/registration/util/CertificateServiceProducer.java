package io.qiot.manufacturing.datacenter.registration.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.qiot.manufacturing.datacenter.registration.certmanager.client.CertificateOperation;
import io.qiot.manufacturing.datacenter.registration.certmanager.client.SecretOperation;
import io.qiot.manufacturing.datacenter.registration.service.CertificateService;
import io.qiot.manufacturing.datacenter.registration.service.impl.CertManagerCertificateService;
import io.qiot.manufacturing.datacenter.registration.service.impl.DefaultCertificateService;

/**
 * @author andreabattaglia
 */
@ApplicationScoped
public class CertificateServiceProducer {

    @ConfigProperty(name = "qiot.cert-manager.issuer")
    String issuer;
    @ConfigProperty(name = "qiot.cert-manager.domain")
    String domain;
    @ConfigProperty(name = "qiot.cert-manager.enabled")
    String enabled;
    @Inject
    Logger LOGGER;

    @Produces
    public CertificateService getLogger(
            CertificateOperation certificateOperation,
            SecretOperation secretOperation) {
        if ("true".equals(enabled)) {

            LOGGER.debug("Cert Manager provisioning is enabled: {}", enabled);
            return new CertManagerCertificateService(certificateOperation,
                    secretOperation, LOGGER, issuer, domain);
        }

        else {

            LOGGER.debug("Self-signed certificate provisioning is enabled: {}",
                    enabled);
            return new DefaultCertificateService(LOGGER);
        }
    }
}