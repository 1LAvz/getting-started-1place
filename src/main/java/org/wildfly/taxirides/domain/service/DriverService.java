package org.wildfly.taxirides.domain.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.api.dto.input.DriverInput;
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.exception.DriverNotFoundException;
import org.wildfly.taxirides.domain.repository.DriverRepository;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class DriverService {

    @Inject
    private DriverRepository driverRepository;

    public List<DriverOutput> listAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();

        List<DriverOutput> driversOutput = drivers.stream()
                .map(driver -> DriverOutput.builder()
                        .id(driver.getId())
                        .name(driver.getName())
                        .licenseNumber(driver.getLicenseNumber())
                        .build()
                ).collect(Collectors.toList());

        return driversOutput;
    }

    public DriverOutput addDriver(DriverInput driverInput) {

        Driver driver = Driver.builder()
                .name(driverInput.getName())
                .licenseNumber(driverInput.getLicenceNumber())
                .build();

        Driver createdDriver = driverRepository.save(driver);

        DriverOutput driverOutput = DriverOutput.builder()
                .id(createdDriver.getId())
                .name(createdDriver.getName())
                .licenseNumber(createdDriver.getLicenseNumber())
                .build();

        return driverOutput;
    }

    public Driver findOrFailDriverBy(Long driverId) {
        Driver driver = driverRepository.findById(driverId);
        if (driver != null) {
            return driver;
        }

        throw new DriverNotFoundException(driverId);
    }
}
