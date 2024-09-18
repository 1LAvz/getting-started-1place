package org.wildfly.taxirides.controllers;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.dto.CreatePassengerInputDTO;
import org.wildfly.taxirides.dto.PassengerOutputDTO;
import org.wildfly.taxirides.entities.Passenger;
import org.wildfly.taxirides.services.PassengerService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassengerController {

    @Inject
    private PassengerService passengerService;

    @GET
    public Response listPassengers() {
        List<Passenger> passengers = passengerService.listAllPassengers();
        List<PassengerOutputDTO> passengerDTOs = passengers.stream()
                .map(passenger -> PassengerOutputDTO.builder()
                        .id(passenger.getId())
                        .firstName(passenger.getFirstName())
                        .lastName(passenger.getLastName())
                        .age(passenger.getAge())
                        .build()
                ).collect(Collectors.toList());
        return Response.ok(passengerDTOs).build();
    }

    @POST
    public Response addPassenger(@Valid CreatePassengerInputDTO input) {
        Passenger passenger = Passenger.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .age(input.getAge())
                .build();

        Passenger createdPassenger = passengerService.addPassenger(passenger);
        return Response.status(Response.Status.CREATED)
                .entity(createdPassenger)  // Include the created driver in the response body
                .build();
    }
}
