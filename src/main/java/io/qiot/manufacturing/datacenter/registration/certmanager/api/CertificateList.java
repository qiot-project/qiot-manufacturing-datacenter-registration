package io.qiot.manufacturing.datacenter.registration.certmanager.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.fabric8.kubernetes.client.CustomResourceList;
import io.qiot.manufacturing.datacenter.registration.certmanager.api.model.Certificate;

/**
 * @author mmascia
 */
@JsonIgnoreProperties({"spec", "status"})
public class CertificateList extends CustomResourceList<Certificate> {
    private static final long serialVersionUID = 1L;
}
