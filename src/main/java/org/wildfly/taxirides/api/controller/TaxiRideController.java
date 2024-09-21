package org.wildfly.taxirides.api.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.api.dto.input.TaxiRideInput;
import org.wildfly.taxirides.api.dto.input.TaxiRideFilterInput;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutput;
import org.wildfly.taxirides.domain.service.TaxiRideService;

import java.util.List;

@Path("/taxi-rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaxiRideController {

    @Inject
    private TaxiRideService taxiRideService;

    @GET
    public Response getTaxiRides(@BeanParam @Valid TaxiRideFilterInput input) {
        List<TaxiRideOutput> taxiRides = taxiRideService.findTaxiRides(input);
        return Response.ok(taxiRides).build();
    }

    @POST
    @Transactional
    public Response addTaxiRide(@Valid TaxiRideInput taxiRideInput) {
        TaxiRideOutput taxiRide = taxiRideService.addTaxiRide(taxiRideInput);

        return Response.status(Response.Status.CREATED)
                .entity(taxiRide)
                .build();
    }

    @PUT
    @Path("/{taxiRideId}")
    @Transactional
    public Response updateTaxiRide(@PathParam("taxiRideId") Long taxiRideId, @Valid TaxiRideInput taxiRideInput) {
        TaxiRideOutput taxiRide = taxiRideService.updateTaxiRide(taxiRideId, taxiRideInput);
        return Response.ok()
                .entity(taxiRide)
                .build();
    }

    @DELETE
    @Path("/{taxiRideId}")
    @Transactional
    public Response deleteTaxiRide(@PathParam("taxiRideId") Long taxiRideId) {
        taxiRideService.deleteTaxiRide(taxiRideId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{taxiRideId}/passengers/{passengerId}")
    @Transactional
    public Response deletePassengerFromTaxiRide(@PathParam("taxiRideId") Long taxiRideId,
                                            @PathParam("passengerId") Long passengerId) {
        taxiRideService.deletePassengerFromTaxiRide(taxiRideId, passengerId);
        return Response.noContent().build();
    }
}
