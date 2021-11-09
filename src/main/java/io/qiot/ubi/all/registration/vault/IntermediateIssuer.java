package io.qiot.ubi.all.registration.vault;

import java.util.Base64;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.qiot.ubi.all.registration.domain.CertificateRequest;
import io.qiot.ubi.all.registration.domain.CertificateResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.vault.VaultPKISecretEngine;
import io.quarkus.vault.VaultPKISecretEngineFactory;
import io.quarkus.vault.VaultSystemBackendEngine;
import io.quarkus.vault.pki.CSRData;
import io.quarkus.vault.pki.CSRData.PEM;
import io.quarkus.vault.pki.CertificateData;
import io.quarkus.vault.pki.DataFormat;
import io.quarkus.vault.pki.GenerateIntermediateCSROptions;
import io.quarkus.vault.pki.GeneratedIntermediateCSRResult;
import io.quarkus.vault.pki.PrivateKeyData;
import io.quarkus.vault.pki.SignIntermediateCAOptions;
import io.quarkus.vault.pki.SignedCertificate;
import io.quarkus.vault.runtime.client.dto.sys.VaultEnableEngineBody;
import io.quarkus.vault.sys.EnableEngineOptions;
import io.quarkus.vault.sys.VaultSecretEngine;

/**
 * @author mmascia
 */
@ApplicationScoped
@RegisterForReflection(targets = { VaultEnableEngineBody.class,
        VaultEnableEngineBody.Config.class })
public class IntermediateIssuer {

    @ConfigProperty(name = "quarkus.kubernetes-client.namespace")
    String namespace;
    @ConfigProperty(name = "qiot.cert-manager.domain")
    String domain;
    @ConfigProperty(name = "qiot.cert-manager.baseDomain")
    String baseDomain;
    @ConfigProperty(name = "qiot.vault.ttl")
    String timeTolive;

    @Inject
    VaultSystemBackendEngine systemBackendEngine;
    @Inject
    VaultPKISecretEngineFactory pkiSecretEngineFactory;

//    @Inject
//    PEMUtils pemUtils;

    @Inject
    Logger LOGGER;

    public CertificateResponse enable(CertificateRequest certificateRequest)
            throws Exception {
        final String mountRootName = namespace + "-pki";
        final String mountIntemediateName = namespace + "-pki-"
                + certificateRequest.name;
        final String commonName = certificateRequest.name + domain;
        final String k8sInternalService = certificateRequest.name + "."
                + namespace + ".svc";
        final String routeEndpoint = certificateRequest.name + "-" + namespace
                + baseDomain;
        final List<String> sAN = List.of(k8sInternalService, routeEndpoint);

        // Enable Intermediate CA
        EnableEngineOptions options = new EnableEngineOptions()
                .setMaxLeaseTimeToLive(timeTolive);
        systemBackendEngine.enable(VaultSecretEngine.PKI, mountIntemediateName,
                certificateRequest.name + " - Intermediate CA", options);

        VaultPKISecretEngine rootPKIEngine = pkiSecretEngineFactory
                .engine(mountRootName);
        VaultPKISecretEngine intermediatePKIEngine = pkiSecretEngineFactory
                .engine(mountIntemediateName);

        // Start Certificate Sign Request
        GenerateIntermediateCSROptions generateOptions = new GenerateIntermediateCSROptions()
                .setSubjectCommonName(commonName)
                .setSubjectAlternativeNames(sAN).setExportPrivateKey(true)
                .setFormat(DataFormat.PEM);
        GeneratedIntermediateCSRResult intermediateCSRResult = intermediatePKIEngine
                .generateIntermediateCSR(generateOptions);

        String tlsKey = ((PrivateKeyData.PEM) intermediateCSRResult.privateKey)
                .getData();

        SignIntermediateCAOptions signIntermediateCAOptions = new SignIntermediateCAOptions()
                .setFormat(DataFormat.PEM).setTimeToLive(timeTolive);

        PEM intermediateCSRPEM = (CSRData.PEM) intermediateCSRResult.csr;
        SignedCertificate signedCertificate = rootPKIEngine.signIntermediateCA(
                intermediateCSRPEM.getData(), signIntermediateCAOptions);

        String tlsCert = ((CertificateData.PEM) signedCertificate.certificate)
                .getData();
        String ca = ((CertificateData.PEM) signedCertificate.issuingCA)
                .getData();

        intermediatePKIEngine.setSignedIntermediateCA(tlsCert);

        // Send to the caller the signed Intermediate CA.
        CertificateResponse response = new CertificateResponse();
        response.tlsCert = Base64.getEncoder()
                .encodeToString(tlsCert.getBytes());
        response.tlsKey = Base64.getEncoder().encodeToString(tlsKey.getBytes());

//        response.keystore = Base64.getEncoder()
//                .encodeToString(pemUtils.toPKCS12(tlsKey, tlsCert,
//                        certificateRequest.keyStorePassword));
//        response.truststore = Base64.getEncoder().encodeToString(
//                pemUtils.toPKCS12(ca, certificateRequest.keyStorePassword));

        return response;
    }
}
