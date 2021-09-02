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
import io.qiot.manufacturing.datacenter.registration.service.CertificateService;

@Path("/register")
@ApplicationScoped
public class RegisterResource {

    // @Inject
    // @RestClient
    // StationServiceClient stationServiceClient;
    @Inject
    CertificateService certificateService;

    @Inject
    Validator validator;

    @Inject
    Logger LOGGER;

    /**
     * Creates a new instance of a `RegisterRequest`.
     */
    @Transactional
    @Path("/factory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CertificateResponse registerFactory(@Valid FactoryCertificateRequest data)
            throws Exception {
        LOGGER.debug("Received registerRequest: {}", data);

        // Set<ConstraintViolation<RegisterRequest>> violations =
        // validator.validate(data);
        // if (!violations.isEmpty()) throw new
        // ValidationException(Arrays.deepToString(violations.toArray()));

        // final String stationId = stationServiceClient.add(data.getSerial(),
        // data.getName(), data.getLongitude(), data.getLatitude());
        // LOGGER.debug(
        // "Successfully provisioned a new station ID ({}) for the registration
        // request \n{}",
        // stationId, data);

        CertificateResponse response = certificateService.provisionFactory(data);

        LOGGER.debug(
                "Successfully provisioned certificates for the registration request \n{}",
                data);

        LOGGER.debug("Create response: {}", response);
        return response;
    }

    /**
     * Creates a new instance of a `RegisterRequest`.
     */
    @Transactional
    @Path("/machinery")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CertificateResponse registerMachinery(
            @Valid MachineryCertificateRequest data) throws Exception {
        LOGGER.debug("Received registerRequest: {}", data);

        CertificateResponse response = certificateService.provisionMachinery(data);

        LOGGER.debug(
                "Successfully provisioned certificates for the registration request \n{}",
                data);

        // response.setId(stationId);

        LOGGER.debug("Create response: {}", response);
        return response;
    }

}
