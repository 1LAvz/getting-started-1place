package org.wildfly.taxirides.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.wildfly.taxirides.api.dto.input.PassengerInput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;

import java.net.URI;
import java.util.List;

/**
 * There are already some mocked data in the import.sql file
 */
@RunAsClient
@ExtendWith(ArquillianExtension.class)
public class PassengerControllerIT {

    @Test
    public void testListPassengers() {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/passengers"))
                    .request()
                    .get();

            assertEquals(200, response.getStatus());
            List<PassengerOutput> passengers = response.readEntity(List.class);
            assertNotNull(passengers);
        }
    }

    @Test
    public void testAddPassenger() {
        PassengerInput newPassenger = new PassengerInput();
        newPassenger.setFirstName("Jane");
        newPassenger.setLastName("Doe");
        newPassenger.setAge(25);

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/passengers"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(newPassenger, MediaType.APPLICATION_JSON));

            assertEquals(201, response.getStatus());
            PassengerOutput createdPassenger = response.readEntity(PassengerOutput.class);
            assertNotNull(createdPassenger);
            assertEquals("Jane", createdPassenger.getFirstName());
            assertEquals("Doe", createdPassenger.getLastName());
            assertEquals(25, createdPassenger.getAge());
        }
    }
}