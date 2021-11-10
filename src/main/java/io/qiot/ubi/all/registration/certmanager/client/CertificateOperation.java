package io.qiot.ubi.all.registration.certmanager.client;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.fabric8.certmanager.api.model.meta.v1.ObjectReference;
import io.fabric8.certmanager.api.model.meta.v1.SecretKeySelector;
import io.fabric8.certmanager.api.model.v1.Certificate;
import io.fabric8.certmanager.api.model.v1.CertificateCondition;
import io.fabric8.certmanager.api.model.v1.CertificateKeystores;
import io.fabric8.certmanager.api.model.v1.CertificateList;
import io.fabric8.certmanager.api.model.v1.CertificateSpec;
import io.fabric8.certmanager.api.model.v1.CertificateStatus;
import io.fabric8.certmanager.api.model.v1.PKCS12Keystore;
import io.fabric8.certmanager.client.DefaultCertManagerClient;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.qiot.ubi.all.registration.domain.CertificateResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;

/**
 * @author mmascia
 */
@ApplicationScoped
@RegisterForReflection(targets = { Certificate.class, CertificateSpec.class,
        CertificateCondition.class, ObjectReference.class,
        CertificateList.class, Map.class, CertificateKeystores.class,
        PKCS12Keystore.class, SecretKeySelector.class,
        CertificateStatus.class })
public class CertificateOperation {

    @ConfigProperty(name = "quarkus.kubernetes-client.namespace")
    String namespace;

    @ConfigProperty(name = "qiot.cert-manager.wait.duration")
    Long duration;

    @Inject
    DefaultKubernetesClient kubernetesClient;

    @Inject
    Logger LOGGER;

    public MixedOperation<Certificate, CertificateList, Resource<Certificate>> operation() {
        return new DefaultCertManagerClient().inNamespace(namespace).v1()
                .certificates();
    }

    public String getNamespace() {
        return namespace;
    }

    public CertificateResponse isReady(String name) {

        Uni<CertificateResponse> uni = Uni.createFrom().emitter(em -> {
            CertificateWatcher watcher = new CertificateWatcher(em);
            Watch watch = this.operation().withName(name).watch(watcher);
            watcher.setWatch(watch);
        });

        return uni.await().atMost(Duration.ofSeconds(duration));
    }

    private final class CertificateWatcher implements Watcher<Certificate> {
        private final UniEmitter<? super CertificateResponse> em;
        private Watch watch;

        private CertificateWatcher(UniEmitter<? super CertificateResponse> em) {
            this.em = em;
        }

        public void setWatch(Watch watch) {
            this.watch = watch;
        }

        @Override
        public void eventReceived(Action action, Certificate resource) {
            if (this.watch != null && Action.MODIFIED.equals(action)) {
                String name = resource.getMetadata().getName();
                LOGGER.info("Watch Certificate {}: {}", action, name);
                List<CertificateCondition> conditions = resource.getStatus()
                        .getConditions();
                if (conditions.size() > 0) {
                    CertificateCondition condition = conditions.get(0);
                    if ("True".equals(condition.getStatus())) {
                        Secret secret = kubernetesClient.secrets()
                                .inNamespace(namespace)
                                .withName(resource.getSpec().getSecretName())
                                .get();
                        if (secret != null) {
                            String keystore = secret.getData()
                                    .get("keystore.p12");
                            String truststore = secret.getData()
                                    .get("truststore.p12");
                            String tlsCert = secret.getData().get("tls.crt");
                            String tlsKey = secret.getData().get("tls.key");

                            if (keystore != null && truststore != null) {
                                CertificateResponse registerResponse = new CertificateResponse();
                                registerResponse.keystore = keystore;
                                registerResponse.truststore = truststore;
                                registerResponse.tlsCert = tlsCert;
                                registerResponse.tlsKey = tlsKey;

                                em.complete(registerResponse);
                                LOGGER.debug("Certificate {} is ready: {}",
                                        name, resource);
                                watch.close();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onClose(WatcherException cause) {
            LOGGER.error("Watch Error: {}", cause);
            em.fail(cause);
        }
    }

}