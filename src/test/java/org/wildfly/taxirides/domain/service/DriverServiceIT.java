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
import org.wildfly.taxirides.api.dto.input.DriverInput;
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.exception.DriverNotFoundException;
import org.wildfly.taxirides.domain.repository.DriverRepository;
import java.util.List;

@ExtendWith(ArquillianExtension.class)
public class DriverServiceIT {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "DriverServiceIT.war")
                .addClass(DriverService.class);
    }

    @Inject
    DriverService driverService;

    @Inject
    DriverRepository driverRepository;

    @Test
    public void testListAllDrivers() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("John Doe");
        driverRepository.save(driver);

        List<DriverOutput> result = driverService.listAllDrivers();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    public void testAddDriver() {
        DriverInput input = new DriverInput();
        input.setName("Jane Doe");

        DriverOutput result = driverService.addDriver(input);

        assertEquals("Jane Doe", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindOrFailDriverBy() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("John Doe");
        driverRepository.save(driver);

        Driver foundDriver = driverService.findOrFailDriverBy(1L);
        assertEquals("John Doe", foundDriver.getName());

        assertThrows(DriverNotFoundException.class, () -> {
            driverService.findOrFailDriverBy(2L);
        });
    }
}
