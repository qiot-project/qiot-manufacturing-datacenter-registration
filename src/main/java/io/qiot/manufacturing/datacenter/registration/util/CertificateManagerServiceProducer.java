package io.qiot.manufacturing.datacenter.registration.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.qiot.manufacturing.datacenter.registration.certmanager.client.CertificateOperation;
import io.qiot.manufacturing.datacenter.registration.certmanager.client.SecretOperation;
import io.qiot.manufacturing.datacenter.registration.service.CertificateService;
import io.qiot.manufacturing.datacenter.registration.service.NameService;
import io.qiot.manufacturing.datacenter.registration.service.impl.CertManagerCertificateService;
import io.qiot.manufacturing.datacenter.registration.service.impl.CertManagerNameService;
import io.qiot.manufacturing.datacenter.registration.service.impl.DefaultCertificateService;
import io.qiot.manufacturing.datacenter.registration.service.impl.DefaultNameService;

/**
 * @author andreabattaglia
 */
@ApplicationScoped
public class CertificateManagerServiceProducer {

    @ConfigProperty(name = "qiot.cert-manager.issuer")
    String issuer;
    @ConfigProperty(name = "qiot.cert-manager.domain")
    String domain;
    @ConfigProperty(name = "qiot.cert-manager.enabled")
    String enabled;
    @Inject
    Logger LOGGER;

    @Produces
    public CertificateService getCertificateService(
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

    @Produces
    public NameService getNameService(
            CertificateOperation certificateOperation,
            SecretOperation secretOperation) {
        if ("true".equals(enabled)) {

            LOGGER.debug("Cert Manager Name Service provisioning is enabled: {}", enabled);
            return new CertManagerNameService(certificateOperation, LOGGER);
        }

        else {

            LOGGER.debug("Default NameService is enabled: {}",
                    enabled);
            return new DefaultNameService();
        }
    }
}