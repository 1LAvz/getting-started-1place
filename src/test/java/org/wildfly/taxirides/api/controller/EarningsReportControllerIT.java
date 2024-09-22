package org.wildfly.taxirides.api.controller;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.wildfly.taxirides.api.dto.input.EarningsReportInput;
import org.wildfly.taxirides.api.dto.output.EarningsReportOutput;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunAsClient
@ExtendWith(ArquillianExtension.class)
public class EarningsReportControllerIT {

    @Test
    public void testGetEarningsReport() {
        EarningsReportInput input = new EarningsReportInput();
        input.setStartDate(LocalDateTime.parse("2023-01-01T00:00:00"));
        input.setEndDate(LocalDateTime.parse("2023-01-31T23:59:59"));
        input.setDriverId(1L);

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(URI.create("http://localhost:8080/api/earnings-report"))
                    .queryParam("startDate", input.getStartDate())
                    .queryParam("endDate", input.getEndDate())
                    .queryParam("driverId", input.getDriverId()) // If needed
                    .request()
                    .get();

            assertEquals(200, response.getStatus());
            List<EarningsReportOutput> report = response.readEntity(new GenericType<>() {});

            assertNotNull(report);
        }
    }
}