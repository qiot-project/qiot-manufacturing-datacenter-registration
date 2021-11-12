package io.qiot.ubi.all.registration.service.impl;

import java.util.Arrays;
import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.fabric8.certmanager.api.model.v1.Certificate;
import io.fabric8.certmanager.api.model.v1.CertificateBuilder;
import io.fabric8.certmanager.api.model.v1.CertificateKeystores;
import io.fabric8.certmanager.api.model.v1.CertificateKeystoresBuilder;
import io.fabric8.certmanager.api.model.v1.CertificateSpecBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.qiot.ubi.all.registration.certmanager.client.CertificateOperation;
import io.qiot.ubi.all.registration.certmanager.client.SecretOperation;
import io.qiot.ubi.all.registration.domain.CertificateRequest;
import io.qiot.ubi.all.registration.domain.CertificateResponse;
import io.qiot.ubi.all.registration.exception.CertificateProvisionException;
import io.qiot.ubi.all.registration.service.CertificateService;

/**
 * @author mmascia
 *
 */
@ApplicationScoped
@Typed(CertManagerCertificateService.class)
public class CertManagerCertificateService implements CertificateService {

    public static final String KEYSTORE_SECRET_PREFIX = "keystore-secret-";
    public static final String KEYSTORE_KEY_PASSWORD = "password";
    public static final String REGISTRATION_QIOT_IO_SERIAL = "registration.qiot.io/serial";
    public static final String REGISTRATION_QIOT_IO_NAME = "registration.qiot.io/name";
    final CertificateOperation certificateOperation;
    final SecretOperation secretOperation;
    final String issuer;
    final String domain;
    final Logger LOGGER;
    final ObjectMapper MAPPER;

    public CertManagerCertificateService(
            CertificateOperation certificateOperation,
            SecretOperation secretOperation, Logger log,
            @ConfigProperty(name = "qiot.cert-manager.issuer") String issuer,
            @ConfigProperty(name = "qiot.cert-manager.domain") String domain) {
        this.certificateOperation = certificateOperation;
        this.secretOperation = secretOperation;
        this.LOGGER = log;
        this.issuer = issuer;
        this.domain = domain;
        this.MAPPER = new ObjectMapper();
    }

    @Override
    public CertificateResponse provision(CertificateRequest data)
            throws CertificateProvisionException {
        try {
            LOGGER.info(
                    "Attempring to generate a certificate from the following data: {}",
                    MAPPER.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(data));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String name = data.id.toString(); // unique name
        LOGGER.info("The UNIQUE_NAME for this request is: {}", name);
        final String commonName = data.name + data.domain + "."
                + certificateOperation.getNamespace() + domain;
        LOGGER.info("The COMMON_NAME for this request is: {}", commonName);

        CertificateKeystores keystores = createKeyStoreSecret(data, name);
        final Certificate certificate = new CertificateBuilder()
                .withNewMetadata().withName(name)
                .withAnnotations(Collections
                        .singletonMap(REGISTRATION_QIOT_IO_SERIAL, data.serial))
                .withLabels(Collections
                        .singletonMap(REGISTRATION_QIOT_IO_NAME, data.name))
                .endMetadata()
                .withSpec(new CertificateSpecBuilder().withSecretName(name)
                        .withCommonName(commonName)
                        .withDnsNames(
                                Arrays.asList(new String[] { commonName }))
                        .withNewIssuerRef().withName(issuer).endIssuerRef()
                        .withKeystores(keystores).withIsCA(data.ca).build())
                .build();

        certificateOperation.operation().create(certificate);

        LOGGER.info(
                "Certificate successfully created for UNIQUE_NAME={} and COMMON_NAME={}: \n{}",
                name, commonName, certificate);

        return certificateOperation.isReady(name);
    }

    private CertificateKeystores createKeyStoreSecret(CertificateRequest data,
            String id) {

        final String keyStorePassword = data.keyStorePassword;
        if (keyStorePassword != null && !"".equals(keyStorePassword)) {
            String secretName = KEYSTORE_SECRET_PREFIX + id;

            LOGGER.debug("Secret Keystore creation {} ", secretName);

            secretOperation.operation().create(new SecretBuilder()
                    .withNewMetadata().withName(secretName)
                    .withAnnotations(Collections.singletonMap(
                            REGISTRATION_QIOT_IO_SERIAL, data.serial))
                    .withLabels(Collections
                            .singletonMap(REGISTRATION_QIOT_IO_NAME, data.name))
                    .endMetadata().withStringData(Collections.singletonMap(
                            KEYSTORE_KEY_PASSWORD, keyStorePassword))
                    .build());

            return new CertificateKeystoresBuilder().withNewPkcs12()
                    .withCreate(true)
                    .withNewPasswordSecretRef(KEYSTORE_KEY_PASSWORD, secretName)
                    .endPkcs12().build();
        }
        throw new IllegalArgumentException(
                "KeyStorePassword is undedifined for serial: " + data.serial
                        + ", name: " + data.name);
    }
}
