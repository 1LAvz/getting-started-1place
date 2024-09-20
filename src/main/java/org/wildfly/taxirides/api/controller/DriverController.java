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
import org.wildfly.taxirides.api.dto.input.DriverInput;
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import org.wildfly.taxirides.domain.service.DriverService;

import java.util.List;

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverController {

    @Inject
    private DriverService driverService;

    @GET
    public Response listDrivers() {
        List<DriverOutput> drivers = driverService.listAllDrivers();
        return Response.ok(drivers).build();
    }

    @POST
    @Transactional
    public Response addDriver(@Valid DriverInput driverInput) {
        DriverOutput createdDriverOutput = driverService.addDriver(driverInput);

        return Response.status(Response.Status.CREATED)
                .entity(createdDriverOutput)
                .build();
    }
}
