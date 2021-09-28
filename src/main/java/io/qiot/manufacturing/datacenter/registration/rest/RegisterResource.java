package io.qiot.manufacturing.datacenter.registration.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import io.qiot.manufacturing.datacenter.commons.domain.registration.CertificateResponse;
import io.qiot.manufacturing.datacenter.commons.domain.registration.FactoryCertificateRequest;
import io.qiot.manufacturing.datacenter.commons.domain.registration.MachineryCertificateRequest;
import io.qiot.manufacturing.datacenter.registration.domain.CertificateRequest;
import io.qiot.manufacturing.datacenter.registration.service.CertificateService;
import io.qiot.manufacturing.datacenter.registration.service.NameService;

@Path("/register")
@ApplicationScoped
public class RegisterResource {

    @Inject
    CertificateService certificateService;

    @Inject
    NameService nameService;

    @Inject
    Validator validator;

    @Inject
    Logger LOGGER;

    /**
     * Creates a new instance of a `CertificateResponse`.
     */
    @Transactional
    @Path("/factory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CertificateResponse registerFactory(@Valid FactoryCertificateRequest data)
            throws Exception {
        LOGGER.debug("Received registerRequest: {}", data);

        CertificateRequest request = new CertificateRequest(data.factoryId, data.name, "", data.serial, data.keyStorePassword);
        CertificateResponse response = certificateService.provision(request);

        LOGGER.debug(
                "Successfully provisioned certificates for the registration request \n{}",
                data);

        LOGGER.debug("Create response: {}", response);
        return response;
    }

    /**
     * Creates a new instance of a `CertificateResponse`.
     */
    @Transactional
    @Path("/machinery")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CertificateResponse registerMachinery(
            @Valid MachineryCertificateRequest data) throws Exception {
        LOGGER.debug("Received registerRequest: {}", data);

        final String factoryName = nameService.getName(data.factoryId);
        CertificateRequest request = new CertificateRequest(data.machineryId, data.name, "."+ factoryName, data.serial, data.keyStorePassword);
        CertificateResponse response = certificateService.provision(request);

        LOGGER.debug(
                "Successfully provisioned certificates for the registration request \n{}",
                data);

        LOGGER.debug("Create response: {}", response);
        return response;
    }

}
