package io.qiot.manufacturing.datacenter.registration.certmanager.client;

import java.time.Duration;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.fabric8.kubernetes.api.model.Condition;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;
import io.qiot.manufacturing.datacenter.commons.domain.registration.RegisterResponse;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.CertificateList;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.Certificate;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.Constants;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.KeystoreSpec;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;

/**
 * @author mmascia
 */
@ApplicationScoped
public class CertificateOperation {


    @ConfigProperty(name = "quarkus.kubernetes-client.namespace")
    String namespace;

    @ConfigProperty(name = "qiot.cert-manager.wait.duration")
    Long duration;

    @Inject
    DefaultKubernetesClient kubernetesClient;

    @Inject
    Logger LOGGER;

    public static CustomResourceDefinition certificate() {
        return new CustomResourceDefinitionBuilder()
                .withApiVersion(Constants.V1_API_VERSION)
                .withKind(Constants.CRD_KIND).withNewMetadata()
                .withName(Certificate.RESOURCE_PLURAL + "." + Constants.RESOURCE_GROUP_NAME).endMetadata().withNewSpec()
                .withScope(Certificate.SCOPE).withGroup(Constants.RESOURCE_GROUP_NAME).withNewNames()
                .withSingular(Certificate.RESOURCE_SINGULAR).withPlural(Certificate.RESOURCE_PLURAL)
                .withKind(Certificate.RESOURCE_KIND).withListKind(Certificate.RESOURCE_LIST_KIND).endNames().endSpec()
                .build();
    }

    public MixedOperation<Certificate, CertificateList, Resource<Certificate>> operation() {

        CustomResourceDefinition crd = certificate();
        KubernetesDeserializer.registerCustomKind(Constants.RESOURCE_GROUP_NAME + "/" + Constants.VERSION,
                Certificate.RESOURCE_KIND, Certificate.class);
        return kubernetesClient.customResources(CustomResourceDefinitionContext.fromCrd(crd), Certificate.class,
                CertificateList.class);
    }

    public String getNamespace() {
        return namespace;
    }

    public RegisterResponse isReady(String name) {

        Uni<RegisterResponse> uni = Uni.createFrom().emitter(em ->  {
            CertificateWatcher watcher = new CertificateWatcher(em);
            Watch watch = this.operation().withName(name).watch(watcher);
            watcher.setWatch(watch);
        });

        return uni.await().atMost(Duration.ofSeconds(duration));
    }

    private final class CertificateWatcher implements Watcher<Certificate> {
        private final UniEmitter<? super RegisterResponse> em;
        private Watch watch;

        private CertificateWatcher(UniEmitter<? super RegisterResponse> em) {
            this.em = em;
        }

        public void setWatch(Watch watch) {
            this.watch = watch;
        }

        @Override
        public void eventReceived(Action action, Certificate resource) {
            if(this.watch != null && Action.MODIFIED.equals(action)) {
                String name = resource.getMetadata().getName();
                LOGGER.info("Watch Certificate {}: {}", action, name);
                List<Condition> conditions = resource.getStatus().getConditions();
                if (conditions.size() > 0) {
                    Condition condition = conditions.get(0);
                    if ("True".equals(condition.getStatus())) {
                        Secret secret = kubernetesClient.secrets()
                            .inNamespace(namespace)
                            .withName(resource.getSpec().getSecretName())
                            .get();
                        String keystore = secret.getData().get(KeystoreSpec.KEYSTORE_KEY_P12);
                        String truststore = secret.getData().get(KeystoreSpec.TRUSTSTORE_KEY_P12);
                        if(keystore != null && truststore != null) {
                            RegisterResponse registerResponse = new RegisterResponse();
                            registerResponse.keystore=keystore;
                            registerResponse.truststore=truststore;
                            em.complete(registerResponse);
                            LOGGER.debug("Certificate {} is ready: {}", name, resource);
                            watch.close();
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