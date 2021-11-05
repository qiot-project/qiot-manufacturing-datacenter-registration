package io.qiot.ubi.all.registration.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import io.qiot.ubi.all.registration.domain.CAIssuerRequest;
import io.qiot.ubi.all.registration.service.IssuerService;

/**
 * @author mmascia
 *
 */
@Path("/issuer")
@ApplicationScoped
public class IssuerResource {

    @Inject
    IssuerService issuerService;


    @Inject
    Logger LOGGER;

   /**
     * Creates a new Issuer resource on the same registration namespace.
     * issuer -> intermediate(s) -> root
     * 
     */
    @Transactional
    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response provisionIssuer( @Valid CAIssuerRequest issuerRequest) {

        LOGGER.debug("Received request: {}", issuerRequest);
        try {
            this.issuerService.provision(issuerRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Status.CREATED).build();
    }
    
}
