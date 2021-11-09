package io.qiot.ubi.all.registration.certmanager.client;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.fabric8.certmanager.api.model.meta.v1.ObjectReference;
import io.fabric8.certmanager.api.model.v1.CAIssuer;
import io.fabric8.certmanager.api.model.v1.Issuer;
import io.fabric8.certmanager.api.model.v1.IssuerCondition;
import io.fabric8.certmanager.api.model.v1.IssuerList;
import io.fabric8.certmanager.api.model.v1.IssuerSpec;
import io.fabric8.certmanager.api.model.v1.IssuerStatus;
import io.fabric8.certmanager.client.DefaultCertManagerClient;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author mmascia
 */
@ApplicationScoped
@RegisterForReflection(targets = { Issuer.class, IssuerSpec.class,
        IssuerCondition.class, ObjectReference.class, IssuerList.class,
        CAIssuer.class, Map.class, List.class, IssuerStatus.class })
public class IssuerOperation {

    @ConfigProperty(name = "quarkus.kubernetes-client.namespace")
    String namespace;

    @ConfigProperty(name = "qiot.cert-manager.wait.duration")
    Long duration;

    @Inject
    DefaultKubernetesClient kubernetesClient;

    @Inject
    Logger LOGGER;

    public MixedOperation<Issuer, IssuerList, Resource<Issuer>> operation() {
        return new DefaultCertManagerClient().inNamespace(namespace).v1()
                .issuers();
    }

    public String getNamespace() {
        return namespace;
    }

}
