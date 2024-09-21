package org.wildfly.taxirides.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.wildfly.taxirides.api.dto.input.PassengerInput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.exception.PassengerNotFoundException;
import org.wildfly.taxirides.domain.repository.PassengerRepository;

import java.util.List;

@ExtendWith(ArquillianExtension.class)
public class PassengerServiceIT {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "PassengerServiceIT.war")
                .addClass(PassengerService.class);
    }

    @Inject
    PassengerService passengerService;

    @Inject
    PassengerRepository passengerRepository;

    @Test
    public void testListAllPassengers() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Alice");
        passenger.setLastName("Smith");
        passengerRepository.save(passenger);

        List<PassengerOutput> result = passengerService.listAllPassengers();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Smith", result.get(0).getLastName());
    }

    @Test
    public void testAddPassenger() {
        PassengerInput input = new PassengerInput();
        input.setFirstName("Bob");
        input.setLastName("Johnson");

        PassengerOutput result = passengerService.addPassenger(input);

        assertEquals("Bob", result.getFirstName());
        assertEquals("Johnson", result.getLastName());
        assertEquals(1L, result.getId()); // Assuming ID is set automatically
    }

    @Test
    public void testFindOrFailPassengerBy() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Alice");
        passenger.setLastName("Smith");
        passengerRepository.save(passenger);

        Passenger foundPassenger = passengerService.findOrFailPassengerBy(1L);
        assertEquals("Alice", foundPassenger.getFirstName());
        assertEquals("Smith", foundPassenger.getLastName());

        assertThrows(PassengerNotFoundException.class, () -> {
            passengerService.findOrFailPassengerBy(2L);
        });
    }
}
