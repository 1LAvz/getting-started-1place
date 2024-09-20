package org.wildfly.taxirides.api.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.api.dto.input.PassengerInput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.domain.service.PassengerService;

import java.util.List;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassengerController {

    @Inject
    private PassengerService passengerService;

    @GET
    public Response listPassengers() {
        List<PassengerOutput> passengers = passengerService.listAllPassengers();
        return Response.ok(passengers).build();
    }

    @POST
    @Transactional
    public Response addPassenger(@Valid PassengerInput passengerInput) {
        PassengerOutput passenger = passengerService.addPassenger(passengerInput);

        return Response.status(Response.Status.CREATED)
                .entity(passenger)
                .build();
    }
}
