package io.qiot.ubi.all.registration.config;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;

/**
 * @author mmascia
 */
@Singleton
public class KubernetesClientProducer {

    @Produces
    public DefaultKubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }
}