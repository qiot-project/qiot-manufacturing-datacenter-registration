package io.qiot.ubi.all.registration.rest;

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

import io.qiot.ubi.all.registration.domain.CertificateRequest;
import io.qiot.ubi.all.registration.domain.CertificateResponse;
import io.qiot.ubi.all.registration.service.CertificateService;
import io.qiot.ubi.all.registration.service.NameService;
import io.qiot.ubi.all.registration.vault.IntermediateIssuer;

@Path("/register")
@ApplicationScoped
public class RegisterResource {

    @Inject
    CertificateService certificateService;

    @Inject
    IntermediateIssuer intermediateIssuer;

    @Inject
    NameService nameService;

    @Inject
    Validator validator;

    @Inject
    Logger LOGGER;

    /**
     * Creates a new instance of a `CertificateResponse`.
     * 
     * If the fiels <code>ca</code> in the certificateRequest object is true,
     * the service will generate a delegate certificate authority for the
     * factory layer
     */
    @Transactional
    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CertificateResponse provisionCertificate(
            @Valid CertificateRequest request) throws Exception {

        LOGGER.debug("Received cartificate request: {}", request);

        CertificateResponse response = null;

        if (request.ca) {
            response = intermediateIssuer.enable(request);
        } else {
            response = certificateService.provision(request);
        }

        LOGGER.debug(
                "Successfully provisioned certificates for the registration request \n{}",
                request);

        LOGGER.debug("Create response: {}", response);
        return response;
    }

    // /**
    // * Creates a new instance of a `CertificateResponse`.
    // */
    // @Transactional
    // @Path("/factory")
    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public CertificateResponse registerFactory(@Valid
    // FactoryCertificateRequest data)
    // throws Exception {
    // LOGGER.debug("Received registerRequest: {}", data);
    //
    // CertificateRequest request = new CertificateRequest(data.factoryId,
    // data.name, "", data.serial, data.keyStorePassword);
    // CertificateResponse response = certificateService.provision(request);
    //
    // LOGGER.debug(
    // "Successfully provisioned certificates for the registration request
    // \n{}",
    // data);
    //
    // LOGGER.debug("Create response: {}", response);
    // return response;
    // }

    // /**
    // * Creates a new instance of a `CertificateResponse`.
    // */
    // @Transactional
    // @Path("/machinery")
    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public CertificateResponse registerMachinery(
    // @Valid MachineryCertificateRequest data) throws Exception {
    // LOGGER.debug("Received registerRequest: {}", data);
    //
    // final String factoryName = nameService.getName(data.factoryId);
    // CertificateRequest request = new CertificateRequest(data.machineryId,
    // data.name, "."+ factoryName, data.serial, data.keyStorePassword);
    // CertificateResponse response = certificateService.provision(request);
    //
    // LOGGER.debug(
    // "Successfully provisioned certificates for the registration request
    // \n{}",
    // data);
    //
    // LOGGER.debug("Create response: {}", response);
    // return response;
    // }

}
