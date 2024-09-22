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
import org.wildfly.taxirides.api.dto.input.DriverInput;
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import java.net.URI;
import java.util.List;

/**
 * There are already some mocked data in the import.sql file
 */
@RunAsClient
@ExtendWith(ArquillianExtension.class)
public class DriverControllerIT {

    @Test
    public void testListDrivers() {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/drivers"))
                    .request()
                    .get();

            assertEquals(200, response.getStatus());
            List<DriverOutput> drivers = response.readEntity(List.class);
            assertNotNull(drivers);
        }
    }

    @Test
    public void testAddDriver() {
        DriverInput newDriver = new DriverInput();
        newDriver.setName("John Doe");
        newDriver.setLicenseNumber("123456789");

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/drivers"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(newDriver, MediaType.APPLICATION_JSON));

            assertEquals(201, response.getStatus());
            DriverOutput createdDriver = response.readEntity(DriverOutput.class);
            assertNotNull(createdDriver);
            assertEquals("John Doe", createdDriver.getName());
            assertEquals("123456789", createdDriver.getLicenseNumber());
        }
    }
}