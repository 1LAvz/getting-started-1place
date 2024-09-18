package org.wildfly.taxirides.domain.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
//import org.wildfly.taxirides.api.exceptionhandler.ExceptionHandler;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.exception.CouldNotSaveDriverException;
import org.wildfly.taxirides.domain.repository.intarface.DriverRepository;

import java.util.List;

//@ExceptionHandler
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
//        if(true) {
//            throw new CouldNotSaveDriverException("Testando nao pode salvar driver exception");
//        }
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
