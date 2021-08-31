package io.qiot.manufacturing.datacenter.registration.certmanager.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * @author mmascia
 */
public interface UnknownPropertyPreserving {

    @JsonAnyGetter
    Map<String, Object> getAdditionalProperties();

    @JsonAnySetter
    void setAdditionalProperty(String name, Object value);
}