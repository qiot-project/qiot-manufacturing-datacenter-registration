package io.qiot.manufacturing.datacenter.registration.service.impl;

import java.util.Arrays;
import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.qiot.manufacturing.datacenter.commons.domain.registration.FactoryRegisterRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.MachineryRegisterRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.RegisterResponse;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.Certificate;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.CertificateKeystoresSpec;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.CertificateSpec;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.KeystoreSpec;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.ObjectReferenceSpec;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.PasswordSecretRefSpec;
import io.qiot.manufacturing.datacenter.registration.certmanager.client.CertificateOperation;
import io.qiot.manufacturing.datacenter.registration.certmanager.client.SecretOperation;
import io.qiot.manufacturing.datacenter.registration.exception.CertificateProvisionException;
import io.qiot.manufacturing.datacenter.registration.service.CertificateService;

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
    final CertificateOperation certificateOperation;
    final SecretOperation secretOperation;
    final String issuer;
    final String domain;
    final Logger LOGGER;

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
    }

    @Override
    public RegisterResponse provisionFactory(FactoryRegisterRequest data)
            throws CertificateProvisionException {
        final String name = data.factoryId.toString(); // unique name
        final String commonName = data.name + "."
                + certificateOperation.getNamespace() + domain;

        final Certificate certificate = Certificate.builder()
                .metadata(new ObjectMetaBuilder().withName(name)
                        .withLabels(Collections.singletonMap(
                                REGISTRATION_QIOT_IO_SERIAL, data.serial))
                        .build())
                .spec(CertificateSpec.builder().secretName(name)
                        .commonName(commonName)
                        .dnsNames(Arrays.asList(new String[] { commonName }))
                        .issuerRef(ObjectReferenceSpec.builder().name(issuer)
                                .build())
                        .keystores(createKeyStoreSecret(data, name)).build())
                .build();

        certificateOperation.operation().create(certificate);

        LOGGER.debug("Certificate creation {} ", certificate);

        return certificateOperation.isReady(name);
    }

    //TODO: adapt to new certificate domain model
    @Override
    public RegisterResponse provisionMachinery(
            MachineryRegisterRequest data)
            throws CertificateProvisionException {
        final String name = data.factoryId.toString(); // unique name
        final String commonName = data.name + "."
                + certificateOperation.getNamespace() + domain;

        final Certificate certificate = Certificate.builder()
                .metadata(new ObjectMetaBuilder().withName(name)
                        .withLabels(Collections.singletonMap(
                                REGISTRATION_QIOT_IO_SERIAL, data.serial))
                        .build())
                .spec(CertificateSpec.builder().secretName(name)
                        .commonName(commonName)
                        .dnsNames(Arrays.asList(new String[] { commonName }))
                        .issuerRef(ObjectReferenceSpec.builder().name(issuer)
                                .build())
                        .keystores(createKeyStoreSecret(data, name)).build())
                .build();

        certificateOperation.operation().create(certificate);

        LOGGER.debug("Certificate creation {} ", certificate);

        return certificateOperation.isReady(name);
    }

    private CertificateKeystoresSpec
            createKeyStoreSecret(FactoryRegisterRequest data, String id) {

        final String keyStorePassword = data.keyStorePassword;

        if (keyStorePassword != null && !"".equals(keyStorePassword)) {
            String secretName = KEYSTORE_SECRET_PREFIX + id;

            LOGGER.debug("Secret Keystore creation {} ", secretName);

            secretOperation.operation().create(new SecretBuilder()
                    .withNewMetadata().withName(secretName)
                    .withLabels(Collections.singletonMap(
                            REGISTRATION_QIOT_IO_SERIAL, data.serial))
                    .endMetadata().withStringData(Collections.singletonMap(
                            KEYSTORE_KEY_PASSWORD, keyStorePassword))
                    .build());

            return CertificateKeystoresSpec.builder()
                    .pkcs12(KeystoreSpec.builder().create(true)
                            .passwordSecretRef(PasswordSecretRefSpec.builder()
                                    .key(KEYSTORE_KEY_PASSWORD).name(secretName)
                                    .build())
                            .build())
                    .build();
        }
        throw new IllegalArgumentException(
                "KeyStorePassword is undedifined for serial: "
                        + data.serial + ", name: " + data.name);
    }

}
