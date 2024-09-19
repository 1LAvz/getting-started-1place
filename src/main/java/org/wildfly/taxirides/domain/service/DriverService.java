package org.wildfly.taxirides.domain.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.exception.DriverNotFoundException;
import org.wildfly.taxirides.domain.repository.intarface.DriverRepository;

import java.util.List;

@Stateless
public class DriverService {

    @Inject
    private DriverRepository driverRepository;

    public List<Driver> listAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver findDriverById(Long id) {
        return driverRepository.findById(id);
    }

    public Driver addDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver findOrFailDriverBy(Long driverId) {
        Driver driver = driverRepository.findById(driverId);
        if (driver != null) {
            return driver;
        }

        throw new DriverNotFoundException(driverId);
    }
}
