package org.wildfly.taxirides.api.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.api.dto.input.CreateTaxiRideDTO;
import org.wildfly.taxirides.api.dto.output.SimpleDriverOutputDTO;
import org.wildfly.taxirides.api.dto.output.SimplePassengerOutputDTO;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutputDTO;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.service.TaxiRideService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/taxiRides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaxiRideController {

    @Inject
    private TaxiRideService taxiRideService;

    @POST
    public Response addTaxiRide(@Valid CreateTaxiRideDTO input) {
        try {
            TaxiRide createdTaxiRide = taxiRideService.addTaxiRide(input);

            SimpleDriverOutputDTO driver = SimpleDriverOutputDTO.builder()
                    .id(createdTaxiRide.getDriver().getId())
                    .name(createdTaxiRide.getDriver().getName())
                    .build();

            List<SimplePassengerOutputDTO> passengers = createdTaxiRide.getPassengers().stream()
                            .map(passenger -> SimplePassengerOutputDTO.builder()
                                    .id(passenger.getId())
                                    .firstName(passenger.getFirstName())
                                    .lastName(passenger.getLastName())
                                    .build())
                                    .collect(Collectors.toList());

            TaxiRideOutputDTO taxiRide = TaxiRideOutputDTO.builder()
                    .id(createdTaxiRide.getId())
                    .cost(createdTaxiRide.getCost())
                    .duration(createdTaxiRide.getDuration())
                    .date(createdTaxiRide.getDate())
                    .driver(driver)
                    .passengers(passengers)
                    .build();

            return Response.status(Response.Status.CREATED).entity(taxiRide).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateTaxiRide(@PathParam("id") Long id, @Valid CreateTaxiRideDTO taxiRideDTO) {
        try {
            taxiRideService.updateTaxiRide(id, taxiRideDTO);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

//    @GET
//    public Response listTaxiRides(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate,
//                                  @QueryParam("minCost") BigDecimal minCost, @QueryParam("maxCost") BigDecimal maxCost,
//                                  @QueryParam("minDuration") Integer minDuration, @QueryParam("maxDuration") Integer maxDuration,
//                                  @QueryParam("driverId") Long driverId, @QueryParam("passengerId") Long passengerId,
//                                  @QueryParam("passengerAge") Integer passengerAge) {
//        List<TaxiRide> taxiRides = taxiRideService.findTaxiRidesWithCriteria(startDate, endDate, minCost, maxCost, minDuration, maxDuration, driverId, passengerId, passengerAge);
//        return Response.ok(taxiRides).build();
//    }
}
