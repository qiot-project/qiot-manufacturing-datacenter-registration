package io.qiot.ubi.all.registration.config;

// package io.qiot.manufacturing.datacenter.registration.config;
//
// import javax.enterprise.context.Dependent;
// import javax.enterprise.inject.Produces;
// import javax.inject.Inject;
//
// import org.eclipse.microprofile.config.inject.ConfigProperty;
// import org.slf4j.Logger;
//
// import
// io.qiot.manufacturing.datacenter.registration.certmanager.client.CertificateOperation;
// import
// io.qiot.manufacturing.datacenter.registration.certmanager.client.SecretOperation;
// import
// io.qiot.manufacturing.datacenter.registration.service.CertificateService;
// import
// io.qiot.manufacturing.datacenter.registration.service.impl.CertManagerCertificateService;
// import
// io.qiot.manufacturing.datacenter.registration.service.impl.DefaultCertificateService;
// import io.quarkus.arc.DefaultBean;
// import io.quarkus.arc.properties.IfBuildProperty;
//
/// **
// * @author mmascia
// */
// @Dependent
// public class CertificateServiceConfiguration {
//
// @ConfigProperty(name = "qiot.cert-manager.issuer")
// String issuer;
// @ConfigProperty(name = "qiot.cert-manager.domain")
// String domain;
// @Inject
// Logger LOGGER;
//
// @Produces
// @IfBuildProperty(name = "qiot.cert-manager.enabled", stringValue = "true")
// public CertificateService certManagerCertificateService(
// CertificateOperation certificateOperation,
// SecretOperation secretOperation) {
// LOGGER.debug("Cert Manager provisioning is enabled.");
// return new CertManagerCertificateService(certificateOperation,
// secretOperation, LOGGER, issuer, domain);
// }
//
// @Produces
// @DefaultBean
// public CertificateService defaultCertificateService() {
// LOGGER.debug("Self-signed certificate provisioning is enabled.");
// return new DefaultCertificateService(LOGGER);
// }
// }
