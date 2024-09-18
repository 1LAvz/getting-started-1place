package org.wildfly.taxirides.api.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.api.dto.input.CreateDriverInputDTO;
import org.wildfly.taxirides.api.dto.output.DriverOutputDTO;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.service.DriverService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverController {

    @Inject
    private DriverService driverService;

    @GET
    public Response listDrivers() {
        List<Driver> drivers = driverService.listAllDrivers();
        List<DriverOutputDTO> driverDTOs = drivers.stream()
                .map(driver -> DriverOutputDTO.builder()
                        .id(driver.getId())
                        .name(driver.getName())
                        .licenceNumber(driver.getLicenceNumber())
                        .build()
                ).collect(Collectors.toList());
        return Response.ok(driverDTOs).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDriver(@Valid CreateDriverInputDTO input) {
        Driver driver = Driver.builder()
                .name(input.getName())
                .licenceNumber(input.getLicenceNumber())
                .build();

        Driver createdDriver = driverService.addDriver(driver);

        return Response.status(Response.Status.CREATED)
                .entity(createdDriver)
                .build();
    }
}
