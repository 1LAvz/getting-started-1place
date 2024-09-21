package org.wildfly.taxirides.domain.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.wildfly.taxirides.domain.converter.DriverConverter;
import org.wildfly.taxirides.api.dto.input.DriverInput;
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.exception.DriverNotFoundException;
import org.wildfly.taxirides.domain.repository.DriverRepository;

import java.util.List;

@Stateless
public class DriverService {

    @Inject
    private DriverRepository driverRepository;

    @Inject
    private DriverConverter driverConverter;

    public List<DriverOutput> listAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return driverConverter.convertToDTO(drivers);
    }

    public DriverOutput addDriver(DriverInput driverInput) {
        Driver driver = driverConverter.convertToEntity(driverInput);
        Driver createdDriver = driverRepository.save(driver);

        return driverConverter.convertToDTO(createdDriver);
    }

    public Driver findOrFailDriverBy(Long driverId) {
        Driver driver = driverRepository.findById(driverId);
        if (driver != null) {
            return driver;
        }

        throw new DriverNotFoundException(driverId);
    }
}
