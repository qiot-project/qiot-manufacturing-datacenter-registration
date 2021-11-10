package io.qiot.ubi.all.registration.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.qiot.ubi.all.registration.certmanager.client.CertificateOperation;
import io.qiot.ubi.all.registration.certmanager.client.IssuerOperation;
import io.qiot.ubi.all.registration.certmanager.client.SecretOperation;
import io.qiot.ubi.all.registration.service.CertificateService;
import io.qiot.ubi.all.registration.service.IssuerService;
import io.qiot.ubi.all.registration.service.NameService;
import io.qiot.ubi.all.registration.service.impl.CertManagerCertificateService;
import io.qiot.ubi.all.registration.service.impl.CertManagerIssuerService;
import io.qiot.ubi.all.registration.service.impl.CertManagerNameService;
import io.qiot.ubi.all.registration.service.impl.DefaultCertificateService;
import io.qiot.ubi.all.registration.service.impl.DefaultIssuerService;
import io.qiot.ubi.all.registration.service.impl.DefaultNameService;

/**
 * @author andreabattaglia
 */
@ApplicationScoped
public class CertificateManagerServiceProducer {

    @ConfigProperty(name = "qiot.cert-manager.issuer")
    String issuer;
    @ConfigProperty(name = "qiot.cert-manager.intermediate.issuer")
    String issuerIntermediate;
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
    public NameService getNameService(CertificateOperation certificateOperation,
            SecretOperation secretOperation) {
        if ("true".equals(enabled)) {

            LOGGER.debug(
                    "Cert Manager Name Service provisioning is enabled: {}",
                    enabled);
            return new CertManagerNameService(certificateOperation, LOGGER);
        }

        else {

            LOGGER.debug("Default NameService is enabled: {}", enabled);
            return new DefaultNameService();
        }
    }

    @Produces
    public IssuerService getIssuerService(IssuerOperation issuerOperation,
            SecretOperation secretOperation) {
        if ("true".equals(enabled)) {

            LOGGER.debug("Cert Manager Issuer provisioning is enabled: {}",
                    enabled);
            return new CertManagerIssuerService(issuerOperation,
                    secretOperation, LOGGER, issuerIntermediate);
        }

        else {

            LOGGER.debug("Default Issuer provisioning is enabled: {}", enabled);
            return new DefaultIssuerService(LOGGER);
        }
    }
}