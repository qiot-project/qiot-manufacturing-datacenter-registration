package io.qiot.ubi.all.registration.service.impl;

import java.util.Collections;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.fabric8.certmanager.api.model.v1.CAIssuer;
import io.fabric8.certmanager.api.model.v1.CAIssuerBuilder;
import io.fabric8.certmanager.api.model.v1.Issuer;
import io.fabric8.certmanager.api.model.v1.IssuerBuilder;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.qiot.ubi.all.registration.certmanager.client.IssuerOperation;
import io.qiot.ubi.all.registration.certmanager.client.SecretOperation;
import io.qiot.ubi.all.registration.domain.CAIssuerRequest;
import io.qiot.ubi.all.registration.exception.IssuerProvisionException;
import io.qiot.ubi.all.registration.service.IssuerService;

/**
 * @author mmascia
 */
@ApplicationScoped
@Typed(CertManagerIssuerService.class)
public class CertManagerIssuerService implements IssuerService {

    private static final String CA_SECRET = "-secret";
    public static final String REGISTRATION_QIOT_IO_NAME = "registration.qiot.io/name";
    final IssuerOperation issuerOperation;
    final SecretOperation secretOperation;
    final String issuerName;
    final Logger LOGGER;

    public CertManagerIssuerService(IssuerOperation issuerOperation,
            SecretOperation secretOperation, Logger log,
            @ConfigProperty(name = "qiot.cert-manager.intermediate.issuer") String issuerName) {

        this.LOGGER = log;
        this.issuerName = issuerName;
        this.secretOperation = secretOperation;
        this.issuerOperation = issuerOperation;
    }

    @Override
    public void provision(CAIssuerRequest issuerRequest)
            throws IssuerProvisionException {

        final String secretName = this.issuerName + CA_SECRET;
        this.createCASecret(issuerRequest, secretName);

        final CAIssuer caIssuer = new CAIssuerBuilder()
                .withSecretName(secretName).build();
        final Issuer issuer = new IssuerBuilder().withNewMetadata()
                .withName(this.issuerName)
                .withLabels(Collections.singletonMap(REGISTRATION_QIOT_IO_NAME,
                        this.issuerName))
                .endMetadata().withNewSpec().withCa(caIssuer).endSpec().build();

        LOGGER.debug("Call issuerOperation: {}", issuer);
        this.issuerOperation.operation().createOrReplace(issuer);
    }

    private void createCASecret(CAIssuerRequest issuerRequest,
            String secretName) throws IssuerProvisionException {

        Secret secret = new SecretBuilder().withNewMetadata()
                .withName(secretName)
                .withLabels(Collections.singletonMap(REGISTRATION_QIOT_IO_NAME,
                        this.issuerName))
                .endMetadata()
                .withData(Map.of(CAIssuerRequest.TLS_KEY, issuerRequest.tlsKey,
                        CAIssuerRequest.TLS_CERT, issuerRequest.tlsCert))
                .build();

        LOGGER.debug("Call secretOperation: {}", secret);
        this.secretOperation.operation().createOrReplace(secret);
    }

}
