package io.qiot.ubi.all.registration.service.impl;

import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import org.slf4j.Logger;

import io.fabric8.certmanager.api.model.v1.Certificate;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.qiot.ubi.all.registration.certmanager.client.CertificateOperation;
import io.qiot.ubi.all.registration.service.NameService;

/**
 * @author mmascia
 */
@ApplicationScoped
@Typed(CertManagerNameService.class)
public class CertManagerNameService implements NameService {

    final CertificateOperation certificateOperation;
    final Logger LOGGER;

    public CertManagerNameService(CertificateOperation certificateOperation,
            Logger LOGGER) {
        this.certificateOperation = certificateOperation;
        this.LOGGER = LOGGER;
    }

    @Override
    public String getName(UUID id) {

        Resource<Certificate> resource = certificateOperation.operation()
                .withName(id.toString());
        Certificate certificate = resource.get();
        if (certificate == null) {
            throw new RuntimeException("Factory " + id + " not registered");
        }
        final Map<String, String> labels = certificate.getMetadata()
                .getLabels();
        return labels
                .get(CertManagerCertificateService.REGISTRATION_QIOT_IO_NAME);
    }

}
