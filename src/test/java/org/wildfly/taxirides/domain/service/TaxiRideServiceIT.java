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
import org.wildfly.taxirides.api.dto.input.TaxiRideInput;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.exception.BusinessException;
import org.wildfly.taxirides.domain.exception.TaxiRideNotFoundException;
import org.wildfly.taxirides.domain.repository.DriverRepository;
import org.wildfly.taxirides.domain.repository.PassengerRepository;
import org.wildfly.taxirides.domain.repository.TaxiRideRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@ExtendWith(ArquillianExtension.class)
public class TaxiRideServiceIT {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "TaxiRideServiceIT.war")
                .addClass(TaxiRideService.class);
    }

    @Inject
    TaxiRideService taxiRideService;

    @Inject
    TaxiRideRepository taxiRideRepository;

    @Inject
    DriverRepository driverRepository;

    @Inject
    PassengerRepository passengerRepository;

    @Test
    public void testAddTaxiRide() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("John Doe");
        driverRepository.save(driver);

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Jane");
        passenger.setLastName("Doe");
        passengerRepository.save(passenger);

        TaxiRideInput input = new TaxiRideInput();
        input.setDriverId(1L);
        input.setCost(new BigDecimal(50));
        input.setDuration(30);

        input.setDate(LocalDateTime.parse("2023-09-17T14:30:00"));
        input.setPassengerIds(Set.of(1L));

        TaxiRideOutput result = taxiRideService.addTaxiRide(input);

        assertEquals(1L, result.getId());
        assertEquals(BigDecimal.valueOf(50), result.getCost());
    }

    @Test
    public void testUpdateTaxiRide() {
        // First, create a taxi ride to update
        TaxiRide taxiRide = new TaxiRide();
        taxiRide.setId(1L);
        taxiRide.setCost(new BigDecimal(30));
        taxiRide.setDuration(20);
        taxiRide.setDate(LocalDateTime.parse("2024-09-20T00:00:00"));
        taxiRideRepository.save(taxiRide);

        TaxiRideInput updateInput = new TaxiRideInput();
        updateInput.setCost(new BigDecimal(60));
        updateInput.setDuration(40);
        updateInput.setDate(LocalDateTime.parse("2024-09-21T00:00:00"));
        updateInput.setPassengerIds(Set.of()); // Assuming no passengers to change

        TaxiRideOutput result = taxiRideService.updateTaxiRide(1L, updateInput);

        assertEquals(BigDecimal.valueOf(60), result.getCost());
        assertEquals(40, result.getDuration());
    }

    @Test
    public void testFindTaxiRideById() {
        TaxiRide taxiRide = new TaxiRide();
        taxiRide.setId(1L);
        taxiRide.setCost(new BigDecimal(30));
        taxiRideRepository.save(taxiRide);

        TaxiRide foundTaxiRide = taxiRideService.findOrFailTaxiRideBy(1L);
        assertEquals(1L, foundTaxiRide.getId());

        assertThrows(TaxiRideNotFoundException.class, () -> {
            taxiRideService.findOrFailTaxiRideBy(2L);
        });
    }

    @Test
    public void testDeleteTaxiRide() {
        TaxiRide taxiRide = new TaxiRide();
        taxiRide.setId(1L);
        taxiRide.setCost(new BigDecimal(30));
        taxiRideRepository.save(taxiRide);

        taxiRideService.deleteTaxiRide(1L);

        assertThrows(TaxiRideNotFoundException.class, () -> {
            taxiRideService.findOrFailTaxiRideBy(1L);
        });
    }

    @Test
    public void testDeletePassengerFromTaxiRide() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("John Doe");
        driverRepository.save(driver);

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Jane");
        passenger.setLastName("Doe");
        passengerRepository.save(passenger);

        TaxiRide taxiRide = new TaxiRide();
        taxiRide.setId(1L);
        taxiRide.getPassengers().add(passenger);
        taxiRide.setDriver(driver);
        taxiRideRepository.save(taxiRide);

        taxiRideService.deletePassengerFromTaxiRide(1L, 1L);

        assertEquals(0, taxiRide.getPassengers().size()); // Ensure the passenger was removed

        assertThrows(BusinessException.class, () -> {
            taxiRideService.deletePassengerFromTaxiRide(1L, 1L);
        });
    }
}
