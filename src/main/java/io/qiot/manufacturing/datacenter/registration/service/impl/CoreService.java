package io.qiot.manufacturing.datacenter.registration.service.impl;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.resteasy.plugins.providers.DocumentProvider;
import org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider;
import org.jboss.resteasy.plugins.providers.IIOImageProvider;
import org.jboss.resteasy.plugins.providers.JaxrsFormProvider;
import org.jboss.resteasy.plugins.providers.SourceProvider;
import org.jboss.resteasy.plugins.providers.sse.SseEventProvider;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author andreabattaglia
 *
 */
@ApplicationScoped
@RegisterForReflection(targets = { IIOImageProvider.class,
        SseEventProvider.class, FormUrlEncodedProvider.class,
        SourceProvider.class, DocumentProvider.class, JaxrsFormProvider.class })
public class CoreService {

}
