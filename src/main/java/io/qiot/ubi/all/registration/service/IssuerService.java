package io.qiot.ubi.all.registration.service;

import io.qiot.ubi.all.registration.domain.CAIssuerRequest;
import io.qiot.ubi.all.registration.exception.IssuerProvisionException;

/**
 * @author mmascia
 *
 **/
public interface IssuerService {

    public void provision(CAIssuerRequest issuerRequest)
            throws IssuerProvisionException;
}
