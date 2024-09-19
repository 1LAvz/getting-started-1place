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
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutput;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.service.TaxiRideService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/taxi-rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaxiRideController {

    @Inject
    private TaxiRideService taxiRideService;

    @GET
    public Response getTaxiRides(@BeanParam TaxiRideFilterInput input) {
        List<TaxiRideOutput> taxiRides = taxiRideService.findTaxiRides(input);
        return Response.ok(taxiRides).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTaxiRide(@Valid TaxiRideInput input) {
        TaxiRide createdTaxiRide = taxiRideService.addTaxiRide(input);

        DriverOutput driver = DriverOutput.builder()
                .id(createdTaxiRide.getDriver().getId())
                .name(createdTaxiRide.getDriver().getName())
                .licenseNumber(createdTaxiRide.getDriver().getLicenseNumber())
                .build();

        List<PassengerOutput> passengers = createdTaxiRide.getPassengers().stream()
                        .map(passenger -> PassengerOutput.builder()
                                .id(passenger.getId())
                                .firstName(passenger.getFirstName())
                                .lastName(passenger.getLastName())
                                .age(passenger.getAge())
                                .build())
                                .collect(Collectors.toList());

        TaxiRideOutput taxiRide = TaxiRideOutput.builder()
                .id(createdTaxiRide.getId())
                .cost(createdTaxiRide.getCost())
                .duration(createdTaxiRide.getDuration())
                .date(createdTaxiRide.getDate())
                .driver(driver)
                .passengers(passengers)
                .build();

        return Response.status(Response.Status.CREATED).entity(taxiRide).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTaxiRide(@PathParam("id") Long id, @Valid TaxiRideInput taxiRideDTO) {
        taxiRideService.updateTaxiRide(id, taxiRideDTO);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{taxiRideId}")
    @Transactional
    public Response deletePassengerFromTaxiRide(@PathParam("taxiRideId") Long taxiRideId) {
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
