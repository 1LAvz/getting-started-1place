package org.wildfly.taxirides.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.wildfly.taxirides.api.dto.input.TaxiRideInput;
import org.wildfly.taxirides.api.dto.input.TaxiRideFilterInput;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutput;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


/**
 * There are already some mocked data in the import.sql file
 */
@RunAsClient
@ExtendWith(ArquillianExtension.class)
public class TaxiRideControllerIT {

    @Test
    public void testGetTaxiRides() {
        try (Client client = ClientBuilder.newClient()) {
            TaxiRideFilterInput filterInput = new TaxiRideFilterInput();
            filterInput.setMaxCost(new BigDecimal("100.00"));

            Response response = client
                    .target(URI.create("http://localhost:8080/api/taxi-rides"))
                    .queryParam("maxCost", filterInput.getMaxCost())
                    .request()
                    .get();

            assertEquals(200, response.getStatus());
            List<TaxiRideOutput> taxiRides = response.readEntity(List.class);
            assertNotNull(taxiRides);
        }
    }

    @Test
    public void shouldFailToAddTaxiRideIfDateNotInformed() {
        TaxiRideInput newTaxiRide = new TaxiRideInput();
        newTaxiRide.setDriverId(1L);
        newTaxiRide.setPassengerIds(Set.of(1L, 2L));
        newTaxiRide.setCost(new BigDecimal("100.00"));
        newTaxiRide.setDuration(20);

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/taxi-rides"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(newTaxiRide, MediaType.APPLICATION_JSON));

            assertEquals(400, response.getStatus());
            assertEquals("{\"details\":{\"date\":\"must not be null\"},\"error\":\"Validation failed\"}", response.readEntity(String.class));
        }
    }

    @Test
    public void shouldFailToUpdateTaxiRideIfAnyFieldNotInformed() {
        Long taxiRideId = 1L;
        TaxiRideInput updatedTaxiRide = new TaxiRideInput();
        updatedTaxiRide.setDriverId(1L);

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/taxi-rides/" + taxiRideId))
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(updatedTaxiRide, MediaType.APPLICATION_JSON));

            assertEquals(400, response.getStatus());
            assertTrue(response.readEntity(String.class).contains("Validation failed"));
        }
    }

    @Test
    public void testDeleteTaxiRide() {
        Long taxiRideId = 1L;

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/taxi-rides/" + taxiRideId))
                    .request()
                    .delete();

            assertEquals(204, response.getStatus());
        }
    }

    @Test
    public void testDeletePassengerFromTaxiRide() {
        Long taxiRideId = 2L;
        Long passengerId = 2L;

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/taxi-rides/" + taxiRideId + "/passengers/" + passengerId))
                    .request()
                    .delete();

            assertEquals(204, response.getStatus());
        }
    }
}