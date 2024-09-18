package org.wildfly.taxirides.services;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.entities.Driver;
import org.wildfly.taxirides.repositories.DriverRepository;

import java.util.List;

@Transactional
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

    public void deleteDriver(Long id) {
        Driver driver = driverRepository.findById(id);
        if (driver != null) {
            driverRepository.delete(driver);
        }
    }

    public Driver updateDriver(Long id, Driver driverDetails) {
        Driver driver = driverRepository.findById(id);
        if (driver != null) {
            driver.setName(driverDetails.getName());
            driver.setLicenceNumber(driverDetails.getLicenceNumber());
            return driverRepository.save(driver);
        }
        return null;
    }
}
