package io.qiot.ubi.all.registration.service.impl;

import java.util.Collections;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Typed;

import org.slf4j.Logger;

import io.qiot.ubi.all.registration.domain.CAIssuerRequest;
import io.qiot.ubi.all.registration.exception.IssuerProvisionException;
import io.qiot.ubi.all.registration.service.IssuerService;

/**
 * @author mmascia
 */
@ApplicationScoped
@Typed(DefaultIssuerService.class)
public class DefaultIssuerService implements IssuerService {

    final Logger LOGGER;

    public DefaultIssuerService(Logger log) {

        this.LOGGER = log;
    }

    @Override
    public void provision(CAIssuerRequest issuerRequest)
            throws IssuerProvisionException {

        this.LOGGER.warn("Not active - only with Cert Manager functionality");
    }

}
